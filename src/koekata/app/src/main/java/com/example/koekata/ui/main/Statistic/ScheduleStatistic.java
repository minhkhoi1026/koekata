package com.example.koekata.ui.main.Statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.R;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ScheduleStatistic extends DaggerFragment {


    public StatisticViewModel statisticViewModel;
    //public FragmentStatisticBinding binding;

    @Inject
    ViewModelProviderFactory providerFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticViewModel =
                new ViewModelProvider(this, providerFactory).get(StatisticViewModel.class);

        //binding = FragmentStatisticBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_statistic_schedule, container, false);

        //return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View statisticView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(statisticView, savedInstanceState);
    }
}
