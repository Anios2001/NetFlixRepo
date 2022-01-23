package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.netflix.Adapter.MainRecyclerAdapter;
import com.example.netflix.Modal.Category;
import com.example.netflix.R;
import com.example.netflix.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TV_series extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
RecyclerView TV_SERIES_RECYCLER;
MainRecyclerAdapter tvseriesrecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_series);
        bottomNavigationView= findViewById(R.id.bottom_navigation_ID);
        TV_SERIES_RECYCLER=findViewById(R.id.tvScreenRecycler);
        Menu menu= bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_icon: {
                        Intent i= new Intent(TV_series.this,MainScreen.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.search_icon:{
                        Intent i= new Intent(TV_series.this,SearchActivity.class);
                        startActivity(i);
                        break;
                    }
                    case R.id.settings_icon:{
                        Intent i= new Intent(TV_series.this,Settings.class);
                        startActivity(i);
                        break;
                    }
                }
                return false;
            }
        });
        ConnectivityManager connectivityManager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || (!networkInfo.isConnected()) || (!networkInfo.isAvailable())){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please TURN ON INTERNET CONNECTION to CONTINUE.");

            builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        else
         getAllMoviesData();
    }
    private void setRecyclerView(List<Category> categoryList2) {
        TV_SERIES_RECYCLER.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        Log.e("SIZE OF LISt",String.valueOf(categoryList2.size()));
        tvseriesrecycler= new MainRecyclerAdapter(this,categoryList2);
        TV_SERIES_RECYCLER.setAdapter(tvseriesrecycler);
    }
    public void getAllMoviesData(){
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Category> categories) {
                        Toast.makeText(TV_series.this,String.valueOf(categories.size()),Toast.LENGTH_LONG).show();
                        setRecyclerView(categories);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(TV_series.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(TV_series.this,"DONE",Toast.LENGTH_LONG).show();
                    }
                }));
    }
}