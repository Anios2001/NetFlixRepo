package com.example.netflix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.netflix.Activities.MainActivity;
import com.example.netflix.Activities.Plan_recharge;
import com.example.netflix.Activities.SignInActivity;
import com.example.netflix.MainScreens.MainScreen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    static int counter=0;
    static int duration= 5000;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    Date valid_date,now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar= findViewById(R.id.initialLoadingBar);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
         firebaseUser= firebaseAuth.getCurrentUser();

        showProgress();
        openPanel();
    }

    private void showProgress() {

        final Timer timer= new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                counter++;
                if(counter==duration){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }
    private void openPanel(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser==null){
                Intent i= new Intent(SplashScreen.this, SignInActivity.class);
                startActivity(i);
                finish();}
                else
                {
                    String userID= firebaseAuth.getUid();
                    DocumentReference documentReference= firebaseFirestore.collection("Users").document(userID);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            valid_date= documentSnapshot.getDate("valid_date");
                            now= Calendar.getInstance().getTime();
                            if(valid_date.compareTo(now)>=0){
                                Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent= new Intent(SplashScreen.this, Plan_recharge.class);
                                intent.putExtra("UID",documentSnapshot.getId());
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        },5000);

    }
}