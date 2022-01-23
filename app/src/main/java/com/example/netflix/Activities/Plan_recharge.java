package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Plan_recharge extends AppCompatActivity implements PaymentResultListener {

    Button pay_btn;
    RadioButton basicToggle,preminiumToggle,standardToggle;
    Date validDate,currentDate;
    String email,uid;
    String name,contact_no;
    FirebaseFirestore firebaseFirestore;
    String plan_name="Basic",plan_cost="249",planFormatcost="249/month";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_recharge);
        Bundle extras= getIntent().getExtras();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid= extras.getString("UID");
        pay_btn= findViewById(R.id.pay_btn);
        progressDialog= new ProgressDialog(this);
        basicToggle= findViewById(R.id.radioBasic_btn);
        preminiumToggle= findViewById(R.id.radioPremium_btn);
        standardToggle = findViewById(R.id.radioSuper_btn);
        preminiumToggle.setChecked(true);
        basicToggle.setOnCheckedChangeListener(new Plan_recharge.Radio_check());
        preminiumToggle.setOnCheckedChangeListener(new Plan_recharge.Radio_check());
        standardToggle.setOnCheckedChangeListener(new Plan_recharge.Radio_check());
        currentDate = Calendar.getInstance().getTime();
        pay_btn.setEnabled(false);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH,1);
        validDate= calendar.getTime();
        firebaseFirestore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name= documentSnapshot.getString("Name");
                        contact_no= documentSnapshot.getString("Contact_no");
                        plan_name= documentSnapshot.getString("Plan_name");
                        plan_cost= documentSnapshot.getString("Plan_cost");
                        planFormatcost= documentSnapshot.getString("Plan_format_cost");
                        email= documentSnapshot.getString("Email");
                        checkAppropiate(plan_name);
                        pay_btn.setEnabled(true);
            }
        });
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPayment();
            }
        });
    }

    private void checkAppropiate(String plan_name) {
        switch (plan_name){
            case "Basic":
            {
                basicToggle.setChecked(true);
                break;
            }
            case "Premium":
            {
                preminiumToggle.setChecked(true);
                break;
            }
            case "Standard":
            {
                standardToggle.setChecked(true);
                break;
            }
        }
    }

    private void initPayment() {
        progressDialog.setTitle("Payment under process");
        progressDialog.show();
            final Activity activity = this;
            try {
                Checkout checkout = new Checkout();
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("description", "NETFLIX_PAYMENT");
                object.put("currency", "INR");
                double amount = Double.parseDouble(plan_cost);
                amount *= 100;
                object.put("amount", amount);
                object.put("prefill.email", email);
                object.put("prefill.contact", contact_no);

                checkout.open(activity, object);

            } catch (Exception e) {
                Log.e("PAYMENT ERROR ", "unable to complete payment", e);
                progressDialog.cancel();
            }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Map<String,Object> updateMap= new HashMap<>();
        updateMap.put("Plan_cost",plan_cost);
        updateMap.put("Plan_name",plan_name);
        updateMap.put("Plan_format_cost",planFormatcost);
        updateMap.put("start_date",currentDate);
        updateMap.put("valid_date",validDate);

        firebaseFirestore.collection("Users").document(uid).update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(Plan_recharge.this, MainScreen.class);

                Toast.makeText(Plan_recharge.this,"Payment and Store data success",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Plan_recharge.this,e.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.cancel();
    }

    class Radio_check implements CompoundButton.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                if(buttonView.getId()==R.id.radioBasic_btn) {
                    plan_name = "Basic";
                    plan_cost = "249";
                    planFormatcost = "249/month";

                    preminiumToggle.setChecked(false);
                    standardToggle.setChecked(false);
                }
                if(buttonView.getId()==R.id.radioPremium_btn) {
                    plan_name = "Premium";
                    plan_cost= "399";
                    planFormatcost="399/month";

                    basicToggle.setChecked(false);
                    standardToggle.setChecked(false);
                }
                if(buttonView.getId()==R.id.radioSuper_btn) {
                    plan_name = "Standard";
                    plan_cost= "678";
                    planFormatcost="678/month";

                    basicToggle.setChecked(false);
                    preminiumToggle.setChecked(false);
                }
            }
        }
    }
}