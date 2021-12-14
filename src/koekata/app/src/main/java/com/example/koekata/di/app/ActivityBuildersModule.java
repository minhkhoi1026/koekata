package com.example.koekata.di.app;

import com.example.koekata.di.auth.AuthModule;
import com.example.koekata.di.auth.AuthViewModelsModule;
import com.example.koekata.di.main.MainFragmentBuildersModule;
import com.example.koekata.di.main.MainModule;
import com.example.koekata.di.main.MainViewModelModule;
import com.example.koekata.ui.auth.AuthActivity;
import com.example.koekata.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

// All inject activities go here
@Module
public abstract class ActivityBuildersModule {

    // Auth Subcomponent modules go here
    @ContributesAndroidInjector(
            modules = {
                    AuthModule.class,
                    AuthViewModelsModule.class,
            }
    )
    abstract AuthActivity contributeAuthActivity();

    // Main Subcomponent modules go here
    @ContributesAndroidInjector(
            modules = {
                    MainModule.class,
                    MainViewModelModule.class,
                    MainFragmentBuildersModule.class,
            }
    )
    abstract MainActivity contributeMainActivity();
}
