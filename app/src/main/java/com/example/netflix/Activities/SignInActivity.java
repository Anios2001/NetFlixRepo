package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class SignInActivity extends AppCompatActivity {
 Button sign_in_btn;
 TextView forgotpass_txt,sign_up_txt;
 ProgressBar signInProgressBar;
 FirebaseAuth firebaseAuth;
 TextInputEditText emailEditText,passwordEditText;
String userID;
 FirebaseFirestore firebaseFirestore;
 DocumentReference documentReference;
 Date now,valid_date;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInProgressBar= findViewById(R.id.signIn_loading);
        forgotpass_txt= findViewById(R.id.forgot_pass_txt);
        sign_up_txt= findViewById(R.id.new_user_txt);
        sign_in_btn= findViewById(R.id.signIn_btn);
        emailEditText= findViewById(R.id.emailEdittext);

        passwordEditText= findViewById(R.id.password_editText);
        FirebaseApp.initializeApp(this);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        signInProgressBar.setVisibility(View.INVISIBLE);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email="",password="";
                email= emailEditText.getText().toString();
                password= passwordEditText.getText().toString();
                if(email.isEmpty()){
                    emailEditText.setError("Don't leave this blank");
                    return;}
                if(password.isEmpty()){
                    passwordEditText.setError("Don't leave this blank");
                    return;}
                signInWithFirebase(email,password);
            }
        });
        forgotpass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailEditText.getText().toString();
                if(!email.isEmpty() && checkEmail(email)){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
                    alertDialog.setTitle("Reset your password");
                    alertDialog.setMessage("Press yes to recive a password Reset Mail.");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(SignInActivity.this,"Reset Link Sent to Your ID",Toast.LENGTH_LONG).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   if(e instanceof FirebaseAuthInvalidCredentialsException)
                                   Toast.makeText(SignInActivity.this,"Email Reset Not Sent: No Such ID, Please Sign up .",Toast.LENGTH_LONG).show();
                                   if(e instanceof FirebaseNetworkException)
                                       Toast.makeText(SignInActivity.this,"Email Reset Not Sent: Please Check Network Connection",Toast.LENGTH_LONG).show();
                               }
                           });
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create().show();
                }
                else
                {
                    Toast.makeText(SignInActivity.this,"Please enter a valid Email Id",Toast.LENGTH_LONG).show();
                }

            }
        });
        sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignInActivity.this,SwipeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean checkPassword(String password) {
        if(password.length() < 10 || password.length() > 20) {
            passwordEditText.setError("ENTER A VALID PASSWORD");
            return false;
        }
        return true;
    }

    private boolean checkEmail(String email) {
        if(email.length() < 10 || !email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            emailEditText.setError("ENTER A VALID EMAIL_ID ");
            return false;
        }
        return true;
    }
    private void signInWithFirebase(String email, String password) {
        signInProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (checkEmail(email) && checkPassword(password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        userID= firebaseAuth.getUid();
                        documentReference= firebaseFirestore.collection("Users").document(userID);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                valid_date= documentSnapshot.getDate("valid_date");
                                now= Calendar.getInstance().getTime();
                                if(valid_date.compareTo(now)>=0){
                                    signInProgressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Intent intent = new Intent(SignInActivity.this, MainScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    signInProgressBar.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                   Intent intent= new Intent(SignInActivity.this,Plan_recharge.class);
                                   intent.putExtra("UID",documentSnapshot.getId());
                                   startActivity(intent);
                                }
                            }
                        });

                    } else {
                        signInProgressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(SignInActivity.this, "Wrong Email or Password Please Sign Up!", Toast.LENGTH_LONG).show();
                        if(task.getException() instanceof FirebaseNetworkException)
                            Toast.makeText(SignInActivity.this,"No internet Connection Please Turn it on",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            signInProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}