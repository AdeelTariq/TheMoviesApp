package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.charts.ChartsApiInteractor;
import com.winterparadox.themovieapp.charts.ChartsApiInteractorImpl;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListApiInteractor;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListApiInteractorImpl;
import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.apiServices.ConfigurationApiService;
import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomeApiInteractorImpl;
import com.winterparadox.themovieapp.home.HomeApiService;
import com.winterparadox.themovieapp.hostAndSearch.HostApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.HostApiInteractorImpl;
import com.winterparadox.themovieapp.hostAndSearch.SearchApiService;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractorImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractorImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiService;
import com.winterparadox.themovieapp.personDetails.PersonApiInteractor;
import com.winterparadox.themovieapp.personDetails.PersonApiInteractorImpl;
import com.winterparadox.themovieapp.personDetails.PersonApiService;

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

    @Provides
    @Singleton
    public PersonApiInteractor providePersonApiInteractor (PersonApiService apiService) {
        return new PersonApiInteractorImpl (apiService);
    }


    @Provides
    @Singleton
    public HostApiInteractor provideHostApiInteractor (ConfigurationApiService apiService,
                                                       ChartsApiService chartsService,
                                                       SearchApiService searchService) {
        return new HostApiInteractorImpl (apiService, chartsService, searchService);
    }

    @Provides
    @Singleton
    public ChartsApiInteractor provideChartsApiInteractor (ConfigurationApiService apiService,
                                                           ChartsApiService chartsService) {
        return new ChartsApiInteractorImpl (apiService, chartsService);
    }

    @Provides
    @Singleton
    public ChartMovieListApiInteractor provideChartMovieListApiInteractor (ChartsApiService
                                                                                       chartsService) {
        return new ChartMovieListApiInteractorImpl (chartsService);
    }


    @Provides
    @Singleton
    public SearchResultApiInteractor provideSearchResultApiInteractor (ChartsApiService
                                                                                   chartsService,
                                                                       SearchApiService
                                                                               searchView) {
        return new SearchResultApiInteractorImpl (chartsService, searchView);
    }

}
