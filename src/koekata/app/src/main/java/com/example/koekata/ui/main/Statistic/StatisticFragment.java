package com.example.koekata.ui.main.Statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.koekata.R;
import com.example.koekata.databinding.FragmentPomodoroBinding;
import com.example.koekata.databinding.FragmentStatisticBinding;
import com.example.koekata.ui.main.Pomodoro.PomodoroViewModel;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class StatisticFragment extends Fragment {


    //public StatisticViewModel statisticViewModel;
    private FragmentStatisticBinding binding;
//    private Button back;
//    private Button forward;
//    private TextView monthYear;
//    private BarChart barChart;
//    private TextView titleStatistic;
//    private TextView dailyStatistic;
//    private TextView monthlyStatistic;
//    private TextView weeklyStatistic;
//    private TextView yearlyStatistic;
//    private TextView totalStatistic;
//    private List<DataEntry> data;
//    ArrayList<BarEntry> barEntryArrayList;
//
//
//    private final String daily = "Day: ";
//    private final String monthly = "Month: ";
//    private final String weekly = "Week: ";
//    private final String yearly = "Year: ";
//    private final String total = "Total: ";
//
//    private final String pomodoroStat = "Total Pomodoro Completion";
//    private final String DailyTaskStat = "Daily-Task Completion Rate";
//    private final String scheduleStat = "Total Schedule Setting Time ";
//
//
    ViewPager2 pager2;
    ViewPagerAdapter viewPagerAdapter;


//    @Inject
//    ViewModelProviderFactory providerFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        statisticViewModel =
//                new ViewModelProvider(this, providerFactory).get(StatisticViewModel.class);

        binding = FragmentStatisticBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View statisticView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(statisticView, savedInstanceState);

        pager2 = statisticView.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        pager2.setAdapter(viewPagerAdapter);


//
//        back = statisticView.findViewById(R.id.back);
//        forward = statisticView.findViewById(R.id.forward);
//        monthYear = statisticView.findViewById(R.id.monthYear);
//        barChart = statisticView.findViewById(R.id.chart);
//        titleStatistic = statisticView.findViewById(R.id.titleStatistic);
//        dailyStatistic = statisticView.findViewById(R.id.dayStatistic);
//        monthlyStatistic = statisticView.findViewById(R.id.monthStatistic);
//        yearlyStatistic = statisticView.findViewById(R.id.yearStatistic);
//        weeklyStatistic = statisticView.findViewById(R.id.weekStatistic);
//        totalStatistic = statisticView.findViewById(R.id.totalStatistic);
//
//        data = new ArrayList<>();
//        init();
//        showCurrentInfo();
//
//
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                statisticViewModel.changeBack();
//                statisticViewModel.initPomodoroStatistic();
//            }
//        });
//
//        forward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                statisticViewModel.changeForward();
//                statisticViewModel.initPomodoroStatistic();
//            }
//        });
//
//
//        statisticViewModel.getTotalPomodoroCompletedPerDay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//                dailyStatistic.setText(daily + String.valueOf(integer));
//            }
//        });
//
//        statisticViewModel.getTotalPomodoroCompletedPerMonth().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//                monthlyStatistic.setText(monthly + String.valueOf(integer));
//            }
//        });
//
//        statisticViewModel.getTotalPomodoroCompletedPerYear().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//                yearlyStatistic.setText(yearly + String.valueOf(integer));
//            }
//        });
//
//        statisticViewModel.getTotalPomodoroCompletedPerWeek().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//                weeklyStatistic.setText(weekly + String.valueOf(integer));
//            }
//        });
//
//        statisticViewModel.getTotalPomodoroCompleted().observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//                totalStatistic.setText(total + String.valueOf(integer));
//            }
//        });
//
//        statisticViewModel.getMonthYear().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                monthYear.setText(s);
//            }
//        });
//
//        statisticViewModel.getPomodoroStatistic().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
//            @Override
//            public void onChanged(ArrayList<Integer> integers) {
//                drawChart(integers);
//            }
//        });
//    }
//
//    private void drawChart(ArrayList<Integer> integers) {
//        barEntryArrayList = new ArrayList<>();
//        barEntryArrayList.clear();
//        ArrayList<String> labelName;
//        labelName = new ArrayList<>();
//
//        for (int i = 0; i < integers.size(); ++i) {
//            barEntryArrayList.add(new BarEntry(i, integers.get(i)));
//            labelName.add(String.valueOf(i+1));
//        }
//        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, pomodoroStat);
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        //barDataSet.setValueTextSize(9f);
//
//        BarData barData = new BarData(barDataSet);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelName));
//
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
//        xAxis.setTextSize(9f);
//        xAxis.setLabelCount(labelName.size());
//
//
//        barChart.setBackgroundColor(Color.WHITE);
//        barChart.setData(barData);
//        //barChart.getDescription().setText("Demo");
//        barChart.invalidate();
//    }
//
//    private void init() {
//
//        statisticViewModel.initPomodoroStatistic();
//    }
//
//    private void showCurrentInfo() {
//        titleStatistic.setText(pomodoroStat);
//        statisticViewModel.countTotalPomodoroCompletedPerDay();
//        statisticViewModel.countTotalPomodoroCompletedPerMonth();
//        statisticViewModel.countTotalPomodoroCompletedPerYear();
//        statisticViewModel.countTotalPomodoroCompletedPerWeek();
//        statisticViewModel.countTotalPomodoroCompleted();


    }

}
