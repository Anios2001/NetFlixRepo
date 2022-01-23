package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.netflix.Adapter.MainRecyclerAdapter;
import com.example.netflix.Adapter.SearchRecyclerAdapter;
import com.example.netflix.Modal.Category;
import com.example.netflix.R;
import com.example.netflix.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
RecyclerView SEARCH_RECYCLER_VIEW;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(SearchActivity.this,MainScreen.class);
        startActivity(i);
    }

    SearchRecyclerAdapter searchRecyclerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem menuItem= menu.findItem(R.id.search_view);
        android.widget.SearchView searchView= (android.widget.SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(searchRecyclerAdapter!=null)
                searchRecyclerAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search for shows,movies,tv_series");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bottomNavigationView= findViewById(R.id.bottom_navigation_ID);
        SEARCH_RECYCLER_VIEW= findViewById(R.id.searchScreenRecycler);
        Menu menu= bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_icon:{
                        Intent i= new Intent(SearchActivity.this,MainScreen.class);
                        startActivity(i);
                        break;}
                    case R.id.search_icon:{

                        break;
                    }
                    case R.id.settings_icon:{
                        Intent i= new Intent(SearchActivity.this,Settings.class);
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
        else {
            getResults();
        }
    }
    public void getResults(){
        CompositeDisposable compositeDisposable= new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<Category> categories) {
                       // Toast.makeText(SearchActivity.this,String.valueOf(categories.size()),Toast.LENGTH_LONG).show();
                        setRecyclerView(categories);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(SearchActivity.this,"No internet Connection Please Turn On",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        //Toast.makeText(SearchActivity.this,"DONE",Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void setRecyclerView(List<Category> categories) {
        SEARCH_RECYCLER_VIEW.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        Log.e("SIZE OF LISt",String.valueOf(categories.size()));
        searchRecyclerAdapter= new SearchRecyclerAdapter(this,categories);
        SEARCH_RECYCLER_VIEW.setAdapter(searchRecyclerAdapter);
    }


}