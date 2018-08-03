package com.winterparadox.themovieapp.dagger;

import android.content.Context;

import com.winterparadox.themovieapp.BuildConfig;
import com.winterparadox.themovieapp.common.retrofit.ApiBuilder;
import com.winterparadox.themovieapp.home.HomeApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adeel on 10/1/2017.
 */
@Module
public class ApiServiceModule {


    @Provides
    @Singleton
    public HomeApiService provideHomeApiService (Context context) {
        return ApiBuilder.build (context, HomeApiService.class, BuildConfig.DEBUG);
    }

}
