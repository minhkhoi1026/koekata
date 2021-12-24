package com.example.koekata.ui.main.Statistic;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.R;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DailyStatistic extends DaggerFragment implements View.OnClickListener{
    public StatisticViewModel statisticViewModel;
    //public FragmentStatisticBinding binding;
    TextView btnPomodoro;
    TextView btnDailyTaskList;
    TextView btnSchedule;
    TextView selectTab;
    ColorStateList notSelectTextColor;

    @Inject
    ViewModelProviderFactory providerFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticViewModel =
                new ViewModelProvider(this, providerFactory).get(StatisticViewModel.class);

        //binding = FragmentStatisticBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_statistic_daily, container, false);

        //return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View statisticView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(statisticView, savedInstanceState);

        btnPomodoro = statisticView.findViewById(R.id.item1);
        btnDailyTaskList = statisticView.findViewById(R.id.item2);
        btnSchedule = statisticView.findViewById(R.id.item3);
        selectTab = statisticView.findViewById(R.id.select);
        notSelectTextColor = btnSchedule.getTextColors();

        btnPomodoro.setOnClickListener(this);
        btnDailyTaskList .setOnClickListener(this);
        btnSchedule.setOnClickListener(this);
        selectTab = statisticView.findViewById(R.id.select);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            selectTab.animate().x(0).setDuration(100);
            btnPomodoro.setTextColor(Color.WHITE);
            btnDailyTaskList.setTextColor(notSelectTextColor);
            btnSchedule.setTextColor(notSelectTextColor);
        } else if (view.getId() == R.id.item2){
            btnPomodoro.setTextColor(notSelectTextColor);
            btnDailyTaskList.setTextColor(Color.WHITE);
            btnSchedule.setTextColor(notSelectTextColor);
            int size = btnDailyTaskList.getWidth();
            selectTab.animate().x(size).setDuration(100);
        } else if (view.getId() == R.id.item3){
            btnPomodoro.setTextColor(notSelectTextColor);
            btnSchedule.setTextColor(Color.WHITE);
            btnDailyTaskList.setTextColor(notSelectTextColor);
            int size = btnDailyTaskList.getWidth() * 2;
            selectTab.animate().x(size).setDuration(100);
        }
    }
}
