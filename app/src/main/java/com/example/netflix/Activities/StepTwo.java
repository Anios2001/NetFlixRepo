package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class StepTwo extends AppCompatActivity {
    String plan_name,plan_cost,planformatcost;
    String email,password;
    TextView sign_in_txt,step_two_txt;
    Button continue_Btn;
    EditText emailEditText,passwordEditText;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        emailEditText = findViewById(R.id.emailEdittext);
        passwordEditText= findViewById(R.id.password_editText);
        sign_in_txt= findViewById(R.id.sign_in_txt);
        step_two_txt= findViewById(R.id.step_two_txt);
        continue_Btn= findViewById(R.id.continue_btn_step2);
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Checking in Database......");
        firebaseAuth= FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        plan_name = extras.getString("PLAN_NAME");
        plan_cost = extras.getString("PLAN_COST");
        planformatcost = extras.getString("PLAN_COST_FORMAT") + "";
        sign_in_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepTwo.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        continue_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= emailEditText.getText().toString();
                password= passwordEditText.getText().toString();
                if(checkEmail(email) && checkPassword(password)) {
                    initContinueProcess();
                }
            }
        });
        SpannableString st = new SpannableString(step_two_txt.getText().toString());
        StyleSpan boldStyle0= new StyleSpan(Typeface.BOLD);
        StyleSpan boldStyle1= new StyleSpan(Typeface.BOLD);
        st.setSpan(boldStyle0,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldStyle1,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void initContinueProcess() {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.cancel();
                    Toast.makeText(StepTwo.this,"Please use login interface to Log IN",Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(StepTwo.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.cancel();
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        emailEditText.setError("Email ID already registered please Log in");
                    }
                    else {
                        Intent intent = new Intent(StepTwo.this, StepThree.class);
                        intent.putExtra("PLAN_NAME", plan_name).putExtra("PLAN_COST", plan_cost)
                                .putExtra("PLAN_COST_FORMAT", planformatcost).putExtra("EMAIL", email).putExtra("PASSWORD", password);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private boolean checkPassword(String password) {
        if(password.length() < 10 || password.length() > 20) {
            passwordEditText.setError("ENTER A VALID PASS WORD");
            return false;
        }
        return true;
    }

    private boolean checkEmail(String email) {
        if(email.length() < 10 || !email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            emailEditText.setError("ENTER A VALID EMAIL ID ");
            return false;
        }
        return true;
    }
}