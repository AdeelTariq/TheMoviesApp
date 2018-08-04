package com.winterparadox.themovieapp;


import android.app.Application;

import com.google.gson.Gson;
import com.winterparadox.themovieapp.common.Singleton;
import com.winterparadox.themovieapp.dagger.AppComponent;
import com.winterparadox.themovieapp.dagger.AppModule;
import com.winterparadox.themovieapp.dagger.DaggerAppComponent;

import javax.inject.Inject;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * Created by Adeel on 10/1/2017.
 */

public class App extends android.app.Application {

    private AppComponent appComponent;

    @Inject Gson gson;

    @Override
    public void onCreate () {
        super.onCreate ();

        CaocConfig.Builder.create ()
                .errorDrawable (R.drawable.ic_bug2) //default: bug image
                .apply ();

        appComponent = initDagger (this);

        getAppComponent ().inject (this);

        Singleton.gson = gson;
    }


    protected AppComponent initDagger (Application application) {
        return DaggerAppComponent.builder ()
                .appModule (new AppModule (application))
                .build ();
    }

    public AppComponent getAppComponent () {
        return appComponent;
    }
}
