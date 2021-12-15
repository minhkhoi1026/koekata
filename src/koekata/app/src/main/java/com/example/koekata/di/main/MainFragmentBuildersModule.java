package com.example.koekata.di.main;

//import com.example.dagger2.ui.main.posts.PostsFragment;
//import com.example.dagger2.ui.main.profile.ProfileFragment;

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
}
