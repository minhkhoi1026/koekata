package com.example.koekata.di.main;


import androidx.lifecycle.ViewModel;

import com.example.koekata.di.app.ViewModelKey;
import com.example.koekata.ui.main.DailyTaskList.DailyTaskListViewModel;
import com.example.koekata.ui.main.Pomodoro.PomodoroViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PomodoroViewModel.class)
    public abstract ViewModel bindPomodoroViewModel(PomodoroViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DailyTaskListViewModel.class)
    public abstract ViewModel bindDailyTaskViewModel(DailyTaskListViewModel viewModel);
}
