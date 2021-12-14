package com.example.koekata.di.app;

import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);
}
