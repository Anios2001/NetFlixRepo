package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netflix.Adapter.gettingStartedAdapter;
import com.example.netflix.R;
import com.testfairy.TestFairy;

public class SwipeScreen extends AppCompatActivity {
    TextView privacy_txtView,sign_in_txtView,help_txtView;
    ViewPager viewPager;
    LinearLayout sliderDotHandle;
    Button getting_started_btn;
    public ImageView[] dots;
    public int dotsCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        TestFairy.begin(this, "SDK-2loYo1nc");
        privacy_txtView = findViewById(R.id.privacy_txt);
        sign_in_txtView= findViewById(R.id.sign_in_txt);
        help_txtView = findViewById(R.id.help_txt);
        viewPager= findViewById(R.id.swipeScreenviewPager);
        getting_started_btn = findViewById(R.id.get_started_id);
        sliderDotHandle= findViewById(R.id.sliderDots);
        gettingStartedAdapter gettingStartedAdapter_object=new gettingStartedAdapter(this);
        viewPager.setAdapter(gettingStartedAdapter_object);
        dotsCount= gettingStartedAdapter_object.getCount();
        dots= new ImageView[dotsCount];
        for(int i=0;i<dotsCount;i++){
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);

            dots[i]= new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactive_dots));
            sliderDotHandle.addView(dots[i],layoutParams);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dots));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dotsCount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactive_dots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       privacy_txtView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));
           }
       });
       sign_in_txtView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             startActivity(new Intent(SwipeScreen.this,SignInActivity.class));
             finish();
           }
       });
       help_txtView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/")));
           }
       });
       getting_started_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(SwipeScreen.this,StepOne.class));
           }
       });
    }
}