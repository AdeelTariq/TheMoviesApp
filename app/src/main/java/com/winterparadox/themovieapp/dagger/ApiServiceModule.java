package com.winterparadox.themovieapp.dagger;

import android.content.Context;

import com.winterparadox.themovieapp.BuildConfig;
import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.retrofit.ApiBuilder;
import com.winterparadox.themovieapp.home.HomeApiService;
import com.winterparadox.themovieapp.hostAndSearch.ConfigurationApiService;
import com.winterparadox.themovieapp.hostAndSearch.SearchApiService;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiService;
import com.winterparadox.themovieapp.personDetails.PersonApiService;

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

    @Provides
    @Singleton
    public MovieDetailsApiService provideMovieDetailsApiService (Context context) {
        return ApiBuilder.build (context, MovieDetailsApiService.class, BuildConfig.DEBUG);
    }

    @Provides
    @Singleton
    public PersonApiService providePersonApiService (Context context) {
        return ApiBuilder.build (context, PersonApiService.class, BuildConfig.DEBUG);
    }


    @Provides
    @Singleton
    public ConfigurationApiService provideConfigurationApiService (Context context) {
        return ApiBuilder.build (context, ConfigurationApiService.class, BuildConfig.DEBUG);
    }

    @Provides
    @Singleton
    public ChartsApiService provideChartsApiService (Context context) {
        return ApiBuilder.build (context, ChartsApiService.class, BuildConfig.DEBUG);
    }

    @Provides
    @Singleton
    public SearchApiService provideSearchApiService (Context context) {
        return ApiBuilder.build (context, SearchApiService.class, BuildConfig.DEBUG);
    }

}
