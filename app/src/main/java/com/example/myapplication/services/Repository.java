package com.example.myapplication.services;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.activities.BaseActivity;
import com.example.myapplication.models.Sport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";

    private RetrofitRoutes service;
    private BaseActivity activity;

    public Repository(BaseActivity activity) {
        this.activity = activity;
        service = RetrofitClient.getInstance(activity).getRetrofit().create(RetrofitRoutes.class);
    }

    public LiveData<List<Sport>> getSPorts() {
        activity.showDialog(true);
        MutableLiveData<List<Sport>> listSports = new MutableLiveData<>();
        service.getSports().enqueue(new Callback<List<Sport>>() {
            @Override
            public void onResponse(Call<List<Sport>> call, Response<List<Sport>> response) {
                Log.i(TAG, "onResponse --> isSuccessful: " + response.isSuccessful() + ", URL: " + call.request().url());
                activity.showDialog(false);
                if (response.code() == 200) {
                    listSports.setValue(response.body());
                } else {
                    handleError(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Sport>> call, Throwable t) {
                activity.showDialog(false);
            }
        });
        return listSports;
    }

    private void handleError(int errorType) {
        switch (errorType) {
            case 400:
                showToast("Bad Request");
                break;
            case 401:
                showToast("Unauthorized");
                break;
            case 403:
                showToast("Forbidden");
                break;
            case 404:
                showToast("Not Found");
                break;
            case 500:
                showToast("Internal Server Error");
                break;
            case 502:
                showToast("Bad Gateway");
                break;
            case 503:
                showToast("Service Unavailable");
                break;
            case 504:
                showToast("Gateway Timeout");
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}
