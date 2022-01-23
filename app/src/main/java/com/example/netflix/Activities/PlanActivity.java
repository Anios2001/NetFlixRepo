package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.netflix.R;

public class PlanActivity extends AppCompatActivity {
 TextView signIn_txt;
 Button continue_btn;
 RadioButton basicToggle,preminiumToggle,standardToggle;
 String plan_name="Basic",plan_cost="249",planFormatcost="249/month";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        signIn_txt = findViewById(R.id.sign_in_txt);
        continue_btn= findViewById(R.id.continue_btn);
        basicToggle= findViewById(R.id.radioBasic_btn);
        preminiumToggle= findViewById(R.id.radioPremium_btn);
        standardToggle = findViewById(R.id.radioSuper_btn);
        preminiumToggle.setChecked(true);
        basicToggle.setOnCheckedChangeListener(new Radio_check());
        preminiumToggle.setOnCheckedChangeListener(new Radio_check());
        standardToggle.setOnCheckedChangeListener(new Radio_check());
        signIn_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this,SignInActivity.class);
                startActivity(intent);
            }

        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this,FinishUpAccount.class);
                intent.putExtra("PLAN_NAME",plan_name).
                        putExtra("PLAN_COST",plan_cost).
                        putExtra("PLAN_COST_FORMAT",planFormatcost);

                startActivity(intent);
            }
        });
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