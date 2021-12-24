package com.example.koekata.di.main;


import androidx.lifecycle.ViewModel;

import com.example.koekata.di.app.ViewModelKey;
import com.example.koekata.ui.main.DailyTaskList.DailyTaskListViewModel;
import com.example.koekata.ui.main.Home.HomeViewModel;
import com.example.koekata.ui.main.Pomodoro.PomodoroViewModel;
import com.example.koekata.ui.main.Statistic.StatisticViewModel;
import com.example.koekata.ui.main.Schedule.ScheduleViewModel;
import com.example.koekata.ui.main.WakeupTime.WakeupTimeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(ProfileViewModel.class)
//    public abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);
//
    @Binds
    @IntoMap
    @ViewModelKey(WakeupTimeViewModel.class)
    public abstract ViewModel bindWakeupTimeViewModel(WakeupTimeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScheduleViewModel.class)
    public abstract ViewModel bindScheduleViewModel(ScheduleViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PomodoroViewModel.class)
    public abstract ViewModel bindPomodoroViewModel(PomodoroViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel.class)
    public abstract ViewModel bindStatisticViewModel(StatisticViewModel viewModel);
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(PostsViewModel.class)
//    public abstract ViewModel bindPostsViewModel(PostsViewModel viewModel);
    @Binds
    @IntoMap
    @ViewModelKey(DailyTaskListViewModel.class)
    public abstract ViewModel bindDailyTaskViewModel(DailyTaskListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    public abstract ViewModel bindHomeViewModel(HomeViewModel viewModel);

}
