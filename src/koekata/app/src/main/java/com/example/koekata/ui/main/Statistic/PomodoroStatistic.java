package com.example.koekata.ui.main.Statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.chart.common.dataentry.DataEntry;
import com.example.koekata.R;
import com.example.koekata.databinding.FragmentStatisticBinding;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PomodoroStatistic extends DaggerFragment {

    public StatisticViewModel statisticViewModel;
    private ImageButton back;
    private ImageButton next;
    private TextView monthYear;
    private BarChart barChart;
    private TextView titleStatistic;
    private TextView dailyStatistic;
    private TextView monthlyStatistic;
    private TextView weeklyStatistic;
    private TextView yearlyStatistic;
    private TextView totalStatistic;
    private List<DataEntry> data;
    ArrayList<BarEntry> barEntryArrayList;
    //public FragmentStatisticBinding binding;

    @Inject
    ViewModelProviderFactory providerFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticViewModel =
                new ViewModelProvider(this, providerFactory).get(StatisticViewModel.class);

        //binding = FragmentStatisticBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.fragment_statistic_pomodoro, container, false);

        //return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View statisticView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(statisticView, savedInstanceState);


        back = statisticView.findViewById(R.id.button_back);
        next = statisticView.findViewById(R.id.button_next);
        monthYear = statisticView.findViewById(R.id.text_date);
        barChart = statisticView.findViewById(R.id.chart);
        titleStatistic = statisticView.findViewById(R.id.text_title);
        dailyStatistic = statisticView.findViewById(R.id.text_day_percent);
        monthlyStatistic = statisticView.findViewById(R.id.text_month_percent);
        yearlyStatistic = statisticView.findViewById(R.id.text_year_percent);
        weeklyStatistic = statisticView.findViewById(R.id.text_week_percent);
        totalStatistic = statisticView.findViewById(R.id.text_total_percent);

        data = new ArrayList<>();
        init();
        showCurrentInfo();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statisticViewModel.changeBack();
                statisticViewModel.initPomodoroStatistic();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statisticViewModel.changeForward();
                statisticViewModel.initPomodoroStatistic();
            }
        });


        statisticViewModel.getTotalPomodoroCompletedPerDay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                dailyStatistic.setText(String.valueOf(integer) + " time(s)");
            }
        });

        statisticViewModel.getTotalPomodoroCompletedPerMonth().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                monthlyStatistic.setText(String.valueOf(integer) + " time(s)");
            }
        });

        statisticViewModel.getTotalPomodoroCompletedPerYear().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                yearlyStatistic.setText(String.valueOf(integer) + " time(s)");
            }
        });

        statisticViewModel.getTotalPomodoroCompletedPerWeek().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                weeklyStatistic.setText(String.valueOf(integer)  + " time(s)");
            }
        });

        statisticViewModel.getTotalPomodoroCompleted().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                totalStatistic.setText(String.valueOf(integer) + " time(s)");
            }
        });

        statisticViewModel.getMonthYear().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                monthYear.setText(s);
            }
        });

        statisticViewModel.getPomodoroStatistic().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                drawChart(integers);
            }
        });
    }

    private void drawChart(ArrayList<Integer> integers) {
        barEntryArrayList = new ArrayList<>();
        barEntryArrayList.clear();
        ArrayList<String> labelName;
        labelName = new ArrayList<>();

        for (int i = 0; i < integers.size(); ++i) {
            barEntryArrayList.add(new BarEntry(i, integers.get(i)));
            labelName.add(String.valueOf(i+1));
        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Total Pomodoro Completion");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        //barDataSet.setValueTextSize(9f);

        BarData barData = new BarData(barDataSet);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(9f);
        xAxis.setLabelCount(labelName.size());


        barChart.setBackgroundColor(Color.WHITE);
        barChart.setData(barData);
        //barChart.getDescription().setText("Demo");
        barChart.invalidate();
    }

    private void init() {
        statisticViewModel.initPomodoroStatistic();
    }

    private void showCurrentInfo() {
        //titleStatistic.setText(pomodoroStat);
        statisticViewModel.countTotalPomodoroCompletedPerDay();
        statisticViewModel.countTotalPomodoroCompletedPerMonth();
        statisticViewModel.countTotalPomodoroCompletedPerYear();
        statisticViewModel.countTotalPomodoroCompletedPerWeek();
        statisticViewModel.countTotalPomodoroCompleted();
    }
}
