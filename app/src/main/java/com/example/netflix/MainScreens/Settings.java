package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.Activities.SignInActivity;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Settings extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(Settings.this,MainScreen.class);
        startActivity(i);
    }

    BottomNavigationView bottomNavigationView;
TextView emailTxt_view,planTxt_view,dateTxt_view;
FirebaseAuth firebaseAuth;
DocumentReference documentReference;
FirebaseUser firebaseUser;
FirebaseFirestore firebaseFirestore;
ProgressDialog progressDialog;
EditText password_editText;
Button signOut_btn,reset_password_Btn;
String userId;
Date valid_date;
String email,plan_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bottomNavigationView= findViewById(R.id.bottom_navigation_ID);
        emailTxt_view= findViewById(R.id.email_settings_id);
        planTxt_view= findViewById(R.id.plan_settings_id);
        dateTxt_view= findViewById(R.id.expire_date_settings_id);
        password_editText= findViewById(R.id.password_reset_editText);
        signOut_btn= findViewById(R.id.sign_out_btn);
        reset_password_Btn= findViewById(R.id.reset_password_btn);
        progressDialog= new ProgressDialog(Settings.this);
        progressDialog.setTitle("Loading Details....");
        progressDialog.show();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            userId= firebaseUser.getUid();
            documentReference= firebaseFirestore.collection("Users").document(userId);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                     valid_date= documentSnapshot.getDate("valid_date");
                     email=documentSnapshot.getString("Email");
                     plan_details= documentSnapshot.getString("Plan_name");
                     planTxt_view.setText(plan_details);
                     emailTxt_view.setText(email);
                     dateTxt_view.setText(valid_date.toString());
                     progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                    Toast.makeText(Settings.this,"No Internet Connection ",Toast.LENGTH_LONG).show();
                    else
                       Toast.makeText(Settings.this,"USER NOT AVAILABLE",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            });
        }
        Menu menu= bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_icon:{
                        Intent i= new Intent(Settings.this,MainScreen.class);
                        startActivity(i);
                        break;}
                    case R.id.search_icon:{
                        Intent i= new Intent(Settings.this,SearchActivity.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.settings_icon:{

                        break;
                    }
                }
                return false;
            }
        });
        signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signOutDialog = new AlertDialog.Builder(Settings.this);
                signOutDialog.setTitle("Do you really want to sign Out ?  ");
                signOutDialog.setMessage("Press yes to sign Out.");
                signOutDialog.setCancelable(false);
                signOutDialog.setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        Intent it= new Intent(Settings.this, SignInActivity.class);
                        startActivity(it);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                signOutDialog.show();

            }
        });
        reset_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ProgressDialog resetProgressDialog= new ProgressDialog(Settings.this);
               resetProgressDialog.setTitle("Checking Authencity...");
               resetProgressDialog.show();
               String passKey= password_editText.getText().toString();
               if(passKey.length()>7){
                   firebaseAuth.signInWithEmailAndPassword(email,passKey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()) {
                               resetProgressDialog.cancel();
                               EditText new_password_txt = new EditText(Settings.this);
                               new_password_txt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                               new_password_txt.setHint("New Password");
                               new_password_txt.setLines(1);
                               AlertDialog.Builder alertDialog = new AlertDialog.Builder(Settings.this);
                               alertDialog.setTitle("Update your password");
                               alertDialog.setView(new_password_txt);
                               alertDialog.setCancelable(false);

                               alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       progressDialog.show();
                                       String new_password = new_password_txt.getText().toString();

                                       if (new_password.length() > 7) {
                                           firebaseUser.updatePassword(new_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   progressDialog.cancel();
                                                   Toast.makeText(Settings.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   Toast.makeText(Settings.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                                                   progressDialog.cancel();
                                               }
                                           });
                                       } else {
                                           Toast.makeText(Settings.this, "Password Too Short ", Toast.LENGTH_SHORT).show();
                                           progressDialog.cancel();
                                       }

                                   }
                               }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               });
                               alertDialog.create().show();
                           }

                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           if(e instanceof FirebaseNetworkException)
                               Toast.makeText(Settings.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                           if(e instanceof FirebaseAuthInvalidCredentialsException) {
                               Toast.makeText(Settings.this, "Please Enter Correct Password", Toast.LENGTH_LONG).show();
                               password_editText.setError("Wrong Password ");
                           }
                           else
                           Toast.makeText(Settings.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                           resetProgressDialog.cancel();
                       }
                   });
               }
               else
               {
                   Toast.makeText(Settings.this,"Password Too Short ",Toast.LENGTH_SHORT).show();
                   password_editText.setError("Password Too short");
                   resetProgressDialog.cancel();
               }
            }
        });
    }
}