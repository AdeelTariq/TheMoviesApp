package com.winterparadox.themovieapp.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adeel on 8/3/2017.
 */

@Module
public class AppModule {

    private Application application;

    public AppModule (Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext () {
        return application;
    }
}
