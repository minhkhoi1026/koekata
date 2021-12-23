package com.example.koekata.di.main;


import com.example.koekata.ui.main.Pomodoro.PomodoroFragment;
import com.example.koekata.ui.main.Statistic.DailyStatistic;
import com.example.koekata.ui.main.Statistic.PomodoroStatistic;
import com.example.koekata.ui.main.Statistic.ScheduleStatistic;
import com.example.koekata.ui.main.Statistic.StatisticFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

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
}
