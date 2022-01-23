package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {

    String plan_name,plan_cost,planformatcost;
    String email,password;
    EditText firstName_edit,lastName_edit,contactno_edit;
    TextView termsTxtView,changeTxtView,planname,plancostDet;
    Button start_member_btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String first_name,last_name,contact_no;
    CheckBox checkBox;
    Date validDate,currentDate;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        Bundle extras= getIntent().getExtras();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog= new ProgressDialog(this);
        plan_name = extras.getString("PLAN_NAME");
        plan_cost = extras.getString("PLAN_COST");
        planformatcost = extras.getString("PLAN_COST_FORMAT") + "";
        email= extras.getString("EMAIL");
        password= extras.getString("PASSWORD");
        start_member_btn= findViewById(R.id.start_member_btn);
        termsTxtView= findViewById(R.id.terms_txt);
        changeTxtView = findViewById(R.id.change_txt);
        checkBox= findViewById(R.id.agree_chk_box);
        planname = findViewById(R.id.plan_name_txt);
        plancostDet= findViewById(R.id.plan_cost_details);
        firstName_edit= findViewById(R.id.first_name_id);
        lastName_edit= findViewById(R.id.last_name_id);
        contactno_edit= findViewById(R.id.contact_no_id);
        firebaseAuth= FirebaseAuth.getInstance();
        planname.setText(plan_name);
        plancostDet.setText(planformatcost);
        SpannableString spannableString= new SpannableString(termsTxtView.getText());
        ClickableSpan clickableSpan= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/termsofuse")));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ClickableSpan clickableSpan2= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        changeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PaymentGateway.this,PlanActivity.class);
                startActivity(intent);
                finish();
            }
        });
        spannableString.setSpan(clickableSpan,50,62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2,64,82,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTxtView.setText(spannableString);
        termsTxtView.setMovementMethod(LinkMovementMethod.getInstance());
        Toast.makeText(this,plan_name + plan_cost + planformatcost +email + password ,Toast.LENGTH_LONG).show();
        start_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               initPayment();
            }
        });
    }

    private void initPayment() {
        progressDialog.setTitle("Payment under process");
        progressDialog.show();
        first_name= firstName_edit.getText().toString();
        last_name=lastName_edit.getText().toString();
        contact_no= contactno_edit.getText().toString();
        if(checkFirstName(first_name) && checkLastName(last_name) && checkContactNo(contact_no) && checkAgreement()) {
            String name = first_name + " " + last_name;
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
            }
        }
        else
        {
          Toast.makeText(this,"Please enter valid info",Toast.LENGTH_LONG).show();
          progressDialog.cancel();
        }
    }

    private boolean checkAgreement() {
        if(checkBox.isChecked())
            return true;
        else {
            Toast.makeText(this,"Please Agree to the terms and conditions to proceed..",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean checkContactNo(String contact_no) {
        if(contact_no.length()!=10){
            contactno_edit.setError("Enter a correct PhoneNo");
            return false;
        }
        return true;
    }
    private boolean checkLastName(String last_name){
        if(last_name.length() > 3 && last_name.matches("[a-z A-z]+"))
            return true;
        else
        {
            lastName_edit.setError("Please Enter a valid first name");
            return false;
        }
    }
    private boolean checkFirstName(String first_name) {
        if(first_name.length() > 3 && first_name.matches("[a-z A-z]+"))
            return true;
        else
        {
            firstName_edit.setError("Please Enter a valid first name");
            return false;
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"PAYMENT SUCCESS",Toast.LENGTH_LONG).show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    DocumentReference documentReference=firebaseFirestore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getUid()));
                    Map<String,Object> data=new HashMap<>();
                    data.put("Email",email);
                    data.put("Name",(first_name + " " +last_name));
                    data.put("Plan_cost",plan_cost);
                    data.put("Plan_name",plan_name);
                    data.put("Plan_format_cost",planformatcost);
                    data.put("Contact_no",contact_no);
                    Calendar calendar= Calendar.getInstance();
                    currentDate= calendar.getTime();
                    calendar.add(Calendar.MONTH,1);
                    validDate=calendar.getTime();
                    data.put("start_date",currentDate);
                    data.put("valid_date",validDate);
                    documentReference.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.cancel();
                            Intent i = new Intent(PaymentGateway.this, MainScreen.class);
                            startActivity(i);
                            finish();
                            }
                            else {
                                progressDialog.cancel();
                                Toast.makeText(PaymentGateway.this, "Error while contacting with server", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else
                {
                    progressDialog.cancel();
                    Toast.makeText(PaymentGateway.this,"ERROR WHILE CREATING NEW USER ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        progressDialog.cancel();
        Toast.makeText(this,"PAYMENT FAILED ERROR "+ i,Toast.LENGTH_LONG).show();
    }
}