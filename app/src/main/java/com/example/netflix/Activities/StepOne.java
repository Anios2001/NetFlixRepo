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

import com.example.netflix.R;

public class StepOne extends AppCompatActivity {
TextView sign_in_txt;
Button see_plans_btn;
TextView step_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        see_plans_btn= findViewById(R.id.see_plans_btn_id);
        sign_in_txt = findViewById(R.id.sign_in_txt);
        step_txt = findViewById(R.id.step_one_txt);
        sign_in_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepOne.this,SignInActivity.class);
                startActivity(intent);
            }

        });
        see_plans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepOne.this,PlanActivity.class);
                startActivity(intent);
            }

        });
        SpannableString st = new SpannableString(step_txt.getText().toString());
        StyleSpan boldStyle0= new StyleSpan(Typeface.BOLD);
        StyleSpan boldStyle1= new StyleSpan(Typeface.BOLD);
        st.setSpan(boldStyle0,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldStyle1,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}