package com.example.koekata.di.app;

import android.app.Application;

import com.example.koekata.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

// All AppComponent modules go here
@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
        }
)

// The AppComponent is like a Server and the Application is like a Client
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        // Bind application's data when constructing AppComponent (optional)
        Builder application(Application application);

        AppComponent build();
    }
}
