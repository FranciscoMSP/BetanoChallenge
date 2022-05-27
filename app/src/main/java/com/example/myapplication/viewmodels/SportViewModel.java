package com.example.myapplication.viewmodels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.activities.BaseActivity;
import com.example.myapplication.models.Sport;
import com.example.myapplication.services.Repository;

import java.util.List;

public class SportViewModel extends ViewModel {
    private MutableLiveData<List<Sport>> listSports;
    private Repository repository;

    public void setActivity(BaseActivity activity){
        repository = new Repository(activity);
    }

    public LiveData<List<Sport>> getSports(Fragment fragment){
        if(listSports == null){
            listSports = new MutableLiveData<>();
            setListPrices(fragment);
        }

        return listSports;
    }

    private void setListPrices(Fragment fragment) {
        repository.getSPorts().observe(fragment, pricePlans -> listSports.setValue(pricePlans));
    }

}
