package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.netflix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MovieDetails extends AppCompatActivity {
  ImageView movieBanner;
  TextView movieName;
  Button play_btn;
  String name,image,fileUrl,MovieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
       movieBanner= findViewById(R.id.movie_banner_id);
       movieName= findViewById(R.id.movieNameTxt);
       play_btn= findViewById(R.id.play_btn_id);
       name= getIntent().getStringExtra("MOVIE_NAME");
       image= getIntent().getStringExtra("IMAGE_URL");
        fileUrl= getIntent().getStringExtra("FILE_URL");
        MovieId= getIntent().getStringExtra("MOVIE_ID");

        Glide.with(this).load(image).into(movieBanner);
        movieName.setText(name);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(MovieDetails.this,VideoPlayer.class);
                i.putExtra("FILE_URL",fileUrl);
                startActivity(i);
            }
        });

    }
}