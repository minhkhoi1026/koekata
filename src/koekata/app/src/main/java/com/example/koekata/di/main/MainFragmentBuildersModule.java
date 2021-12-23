package com.example.koekata.di.main;


import com.example.koekata.ui.main.DailyTaskList.DailyTaskListEditFragment;
import com.example.koekata.ui.main.DailyTaskList.DailyTaskListFragment;
import com.example.koekata.ui.main.Home.HomeFragment;
import com.example.koekata.ui.main.Pomodoro.PomodoroFragment;
import com.example.koekata.ui.main.Statistic.DailyStatistic;
import com.example.koekata.ui.main.Statistic.PomodoroStatistic;
import com.example.koekata.ui.main.Statistic.ScheduleStatistic;
import com.example.koekata.ui.main.Statistic.StatisticFragment;

import com.example.koekata.ui.main.Schedule.ScheduleFragment;
import com.example.koekata.ui.main.WakeupTime.WakeupTimeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {


//    @ContributesAndroidInjector
//    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract WakeupTimeFragment contributeWakeupTimeFragment();

    @ContributesAndroidInjector
    abstract ScheduleFragment contributeScheduleFragment();

    @ContributesAndroidInjector
    abstract PomodoroFragment contributePomodoroFragment();
    @ContributesAndroidInjector
    abstract PomodoroStatistic contributePomodoroStatistic();
    @ContributesAndroidInjector
    abstract DailyStatistic contributeDailyStatistic();
    @ContributesAndroidInjector
    abstract ScheduleStatistic contributeScheduleStatistic();
//
//    @ContributesAndroidInjector
//    abstract PostsFragment contributePostsFragment();

    @ContributesAndroidInjector
    abstract DailyTaskListFragment contributeDailyTaskListFragment();

    @ContributesAndroidInjector
    abstract DailyTaskListEditFragment contributeDailyTaskListEditFragment();

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

}
