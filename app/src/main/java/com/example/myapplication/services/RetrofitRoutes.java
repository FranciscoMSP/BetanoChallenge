package com.example.myapplication.services;

import com.example.myapplication.models.Sport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RetrofitRoutes {
    @Headers("Content-Type: application/json")
    @GET("sports")
    Call<List<Sport>> getSports();
}
