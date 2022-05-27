package com.example.myapplication.services;

import android.content.Context;

import okhttp3.Request;
import retrofit2.Retrofit;
import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final RetrofitClient ourInstance = new RetrofitClient();

    private static Context context;
    private static Retrofit retrofit = null;

    String host = "https://618d3aa7fe09aa001744060a.mockapi.io/api/";

    public static RetrofitClient getInstance(Context ctx) {
        context = ctx;
        return ourInstance;
    }

    private RetrofitClient() {}

    public Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .build();
            return chain.proceed(newRequest);
        }).cache(null).build();

        if (retrofit == null) {
            return retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            return retrofit;
        }
    }
}
