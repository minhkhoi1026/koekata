package com.example.koekata.ui.main.Statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.chart.common.dataentry.DataEntry;
import com.example.koekata.R;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DailyStatistic extends DaggerFragment {
    //public StatisticViewModel statisticViewModel;
    //public FragmentStatisticBinding binding;
//    TextView btnPomodoro;
//    TextView btnDailyTaskList;
//    TextView btnSchedule;
//    TextView selectTab;
//    ColorStateList notSelectTextColor;
    private Semaphore mutex;
    private Semaphore mutexStatistic;
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

    private ArrayList<Integer> totalNum;
    private ArrayList<Integer> totalDen;


    private ArrayList<Integer> statisticNum;
    private ArrayList<Integer> statisticDen;


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

        totalNum = new ArrayList<Integer>(Collections.nCopies(5,0));
        totalDen = new ArrayList<Integer>(Collections.nCopies(5,0));
        statisticNum = new ArrayList<Integer>(Collections.nCopies(31,0));
        statisticDen = new ArrayList<Integer>(Collections.nCopies(31,0));

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

        mutex = new Semaphore(1);
        mutexStatistic = new Semaphore(1);
        data = new ArrayList<>();
        init();
        showCurrentInfo();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statisticViewModel.changeBack();
                statisticViewModel.initDailyTaskNum();
                statisticViewModel.initDailyTaskDen();
                //statisticViewModel.initDailyTaskListStatistic();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statisticViewModel.changeForward();
                statisticViewModel.initDailyTaskNum();
                statisticViewModel.initDailyTaskDen();
                //statisticViewModel.initDailyTaskListStatistic();
            }
        });

//        statisticViewModel.getTotalDailyTasksCompleted().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
//            @Override
//            public void onChanged(ArrayList<Integer> integers) {
//                dailyStatistic.setText(String.valueOf(integers.get(0)) + "%");
//                weeklyStatistic.setText(String.valueOf(integers.get(1)) + "%");
//                monthlyStatistic.setText(String.valueOf(integers.get(2)) + "%");
//                yearlyStatistic.setText(String.valueOf(integers.get(3)) + "%");
//                totalStatistic.setText(String.valueOf(integers.get(4)) + "%");
//            }
//        });

        statisticViewModel.getTotalDailyTaskNum().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                try {
                    mutex.acquire();
                    totalNum = integers;
                    updatee();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    mutex.release();
                }
            }
        });

        statisticViewModel.getTotalDailyTaskDen().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                try {
                    mutex.acquire();
                    totalDen = integers;
                    updatee();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    mutex.release();
                }
            }
        });



        statisticViewModel.getDailyTaskNum().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                try {
                    mutex.acquire();
                    statisticNum = integers;
                    draw_chart();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    mutex.release();
                }
            }
        });


        statisticViewModel.getDailyTaskDen().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                try {
                    mutex.acquire();
                    statisticDen = integers;
                    draw_chart();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    mutex.release();
                }
            }
        });



//        statisticViewModel.getDai().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
//            @Override
//            public void onChanged(ArrayList<Integer> integers) {
//                totalNum = integers;
//                updatee();
//            }
//        });
//
//        statisticViewModel.getTotalDailyTaskDen().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
//            @Override
//            public void onChanged(ArrayList<Integer> integers) {
//                totalDen = integers;
//                updatee();
//            }
//        });



        statisticViewModel.getMonthYear().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                monthYear.setText(s);
            }
        });

        statisticViewModel.getDailyTasksStatistic().observe(getViewLifecycleOwner(), new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                drawChart(integers);
            }
        });
    }

    private void draw_chart() {
        barEntryArrayList = new ArrayList<>();
        barEntryArrayList.clear();
        ArrayList<String> labelName;
        labelName = new ArrayList<>();
        int s = statisticNum.size();

        for (int i = 0; i < s; ++i) {
            if((statisticNum.get(i) !=null)&&(statisticDen.get(i)!=null)){
                if(statisticDen.get(i)!=0)
                {
                    barEntryArrayList.add(new BarEntry(i, 100*statisticNum.get(i)/statisticDen.get(i)));
                }
                else
                {
                    barEntryArrayList.add(new BarEntry(i, 0));
                }
                labelName.add(String.valueOf(i+1));
            }


        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Daily-task Completation Rate");
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

    private void updatee() {
        Log.d("Daily Statistic Num:", totalNum.toString());
        Log.d("Daily Statistic Den:", totalDen.toString());

        dailyStatistic.setText(totalDen.get(0) == 0 ? String.valueOf(0) + "%": String.valueOf(100* totalNum.get(0)/ totalDen.get(0))+"%");
        weeklyStatistic.setText(totalDen.get(1) == 0 ? String.valueOf(0) + "%": String.valueOf(100* totalNum.get(1)/ totalDen.get(1))+"%");
        monthlyStatistic.setText(totalDen.get(2) == 0 ? String.valueOf(0) + "%": String.valueOf(100* totalNum.get(2)/ totalDen.get(2))+"%");
        yearlyStatistic.setText(totalDen.get(3) == 0 ? String.valueOf(0) + "%": String.valueOf(100* totalNum.get(3)/ totalDen.get(3))+"%");
        totalStatistic.setText(totalDen.get(4) == 0 ? String.valueOf(0) + "%": String.valueOf(100* totalNum.get(4)/ totalDen.get(4))+"%");
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
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Daily-task Completation Rate");
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
        statisticViewModel.initDailyTaskNum();
        statisticViewModel.initDailyTaskDen();
        //statisticViewModel.initDailyTaskListStatistic();
    }

    private void showCurrentInfo() {
        //titleStatistic.setText(pomodoroStat);
        statisticViewModel.getTotalDailyTaskListNum();
        statisticViewModel.getTotalDailyTaskListDen();
        //statisticViewModel.getTotalDailyTaskListCompleted();


//        btnPomodoro = statisticView.findViewById(R.id.item1);
//        btnDailyTaskList = statisticView.findViewById(R.id.item2);
//        btnSchedule = statisticView.findViewById(R.id.item3);
//        selectTab = statisticView.findViewById(R.id.select);
//        notSelectTextColor = btnSchedule.getTextColors();
//
//        btnPomodoro.setOnClickListener(this);
//        btnDailyTaskList .setOnClickListener(this);
//        btnSchedule.setOnClickListener(this);
//        selectTab = statisticView.findViewById(R.id.select);

    }

//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.item1){
//            selectTab.animate().x(0).setDuration(100);
//            btnPomodoro.setTextColor(Color.WHITE);
//            btnDailyTaskList.setTextColor(notSelectTextColor);
//            btnSchedule.setTextColor(notSelectTextColor);
//        } else if (view.getId() == R.id.item2){
//            btnPomodoro.setTextColor(notSelectTextColor);
//            btnDailyTaskList.setTextColor(Color.WHITE);
//            btnSchedule.setTextColor(notSelectTextColor);
//            int size = btnDailyTaskList.getWidth();
//            selectTab.animate().x(size).setDuration(100);
//        } else if (view.getId() == R.id.item3){
//            btnPomodoro.setTextColor(notSelectTextColor);
//            btnSchedule.setTextColor(Color.WHITE);
//            btnDailyTaskList.setTextColor(notSelectTextColor);
//            int size = btnDailyTaskList.getWidth() * 2;
//            selectTab.animate().x(size).setDuration(100);
//        }
//    }
}
