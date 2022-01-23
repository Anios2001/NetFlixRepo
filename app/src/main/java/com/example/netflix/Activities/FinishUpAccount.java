package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.R;

public class FinishUpAccount extends AppCompatActivity {
 String plan_name,plan_cost,planformatcost;
 TextView sign_in_txt,step_one_txt;
 Button continue_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_account);
        Bundle bundle= this.getIntent().getExtras();
        plan_name = bundle.getString("PLAN_NAME") + " ";
        plan_cost = bundle.getString("PLAN_COST") + " ";
        planformatcost = bundle.getString("PLAN_COST_FORMAT") + " ";
        sign_in_txt = findViewById(R.id.sign_in_txt);
        step_one_txt= findViewById(R.id.step_one_txt);
        continue_btn= findViewById(R.id.continue_btn_finish);

        step_one_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sign_in_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishUpAccount.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishUpAccount.this,StepTwo.class);
                intent.putExtra("PLAN_NAME",plan_name);
                intent.putExtra("PLAN_COST",plan_cost);
                intent.putExtra("PLAN_COST_FORMAT",planformatcost);
                startActivity(intent);
            }
        });
        Toast.makeText(this,plan_name + plan_cost
        + planformatcost,Toast.LENGTH_LONG).show();
        SpannableString st = new SpannableString(step_one_txt.getText().toString());
        StyleSpan boldStyle0= new StyleSpan(Typeface.BOLD);
        StyleSpan boldStyle1= new StyleSpan(Typeface.BOLD);
        st.setSpan(boldStyle0,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldStyle1,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}