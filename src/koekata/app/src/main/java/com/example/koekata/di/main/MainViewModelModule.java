package com.example.koekata.di.main;

//import com.example.dagger2.ui.main.posts.PostsViewModel;
//import com.example.dagger2.ui.main.profile.ProfileViewModel;

import androidx.lifecycle.ViewModel;

import com.example.koekata.di.app.ViewModelKey;
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
}
