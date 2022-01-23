package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netflix.R;

public class StepThree extends AppCompatActivity {
    String plan_name,plan_cost,planformatcost;
    String email,password;
    TextView signOut,step_three_txt;
    LinearLayout paymentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);
        signOut= findViewById(R.id.sign_out_txt);
        Bundle extras= getIntent().getExtras();
        plan_name = extras.getString("PLAN_NAME");
        plan_cost = extras.getString("PLAN_COST");
        planformatcost = extras.getString("PLAN_COST_FORMAT") + "";
        email= extras.getString("EMAIL");
        password= extras.getString("PASSWORD");
        step_three_txt= findViewById(R.id.step_three_txt);
        paymentLinearLayout= findViewById(R.id.payment_init_id);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThree.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        paymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StepThree.this,PaymentGateway.class);
                i.putExtra("EMAIL",email).putExtra("PASSWORD",password).putExtra("PLAN_NAME",plan_name)
                .putExtra("PLAN_COST",plan_cost).putExtra("PLAN_COST_FORMAT",planformatcost);
                startActivity(i);
            }
        });
        SpannableString st = new SpannableString(step_three_txt.getText().toString());
        StyleSpan boldStyle0= new StyleSpan(Typeface.BOLD);
        StyleSpan boldStyle1= new StyleSpan(Typeface.BOLD);
        st.setSpan(boldStyle0,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldStyle1,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


    }
}