package com.example.myapplication.fragments;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.SportsAdapter;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.models.Event;
import com.example.myapplication.models.Sport;
import com.example.myapplication.viewmodels.SportViewModel;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SportViewModel sportViewModel;
    private SportsAdapter sportsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() != null) {
            sportViewModel = new ViewModelProvider(getActivity()).get(SportViewModel.class);
            sportViewModel.setActivity((MainActivity) getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        getSports();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //region API
    private void getSports() {
        if(sportViewModel != null) {
            sportViewModel.getSports(this).observe(getViewLifecycleOwner(), listSports -> {
                if(listSports != null && listSports.size() > 0) {
                    for(Sport s : listSports) {
                        for(Event e : s.getE()) {
                            String[] teams = e.getD().split(" - ");
                            if(teams.length == 2) {
                                e.setD1(teams[0]);
                                e.setD2(teams[1]);
                            }
                        }
                    }

                    setAdapter(listSports);
                }
            });
        }
    }
    //endregion

    //region Adapters
    private void setAdapter(List<Sport> list) {
        sportsAdapter = new SportsAdapter(list, getContext());
        binding.rvSports.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvSports.setAdapter(sportsAdapter);
        sportsAdapter.setArrowClickListener(position -> {
            list.get(position).setExpanded(!list.get(position).isExpanded());
            sportsAdapter.notifyItemChanged(position);
        });
    }
    //endregion

}