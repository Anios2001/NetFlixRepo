package com.example.netflix.Retrofit;

import com.example.netflix.Modal.Category;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.netflix.Retrofit.RetrofitClient.BASE_URL;

public interface ApiInterface {
    @GET(BASE_URL)
    Observable<List<Category>> getCategories();
}
