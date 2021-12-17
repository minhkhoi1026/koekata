package com.example.koekata.di.main;


import com.example.koekata.ui.main.DailyTaskList.DailyTaskListFragment;
import com.example.koekata.ui.main.Pomodoro.PomodoroFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract PomodoroFragment contributePomodoroFragment();

    @ContributesAndroidInjector
    abstract DailyTaskListFragment contributeDailyTaskListFragment();
}
