package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomeApiInteractorImpl;
import com.winterparadox.themovieapp.home.HomeApiService;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractorImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adeel on 10/1/2017.
 */
@Module
public class InteractorModule {


    @Provides
    @Singleton
    public HomeApiInteractor provideHomeApiInteractor (HomeApiService apiService) {
        return new HomeApiInteractorImpl (apiService);
    }

    @Provides
    @Singleton
    public MovieDetailsApiInteractor provideMovieDetailsApiInteractor (MovieDetailsApiService
                                                                               apiService) {
        return new MovieDetailsApiInteractorImpl (apiService);
    }

}
