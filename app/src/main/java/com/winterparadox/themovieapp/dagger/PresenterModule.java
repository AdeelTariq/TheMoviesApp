package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomePresenter;
import com.winterparadox.themovieapp.home.HomePresenterImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenter;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenterImpl;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by Adeel on 10/1/2017.
 */

@Module
public class PresenterModule {

    @Provides
    public HomePresenter provideHomePresenter (HomeApiInteractor apiInteractor, Scheduler
            mainScheduler) {
        return new HomePresenterImpl (apiInteractor, mainScheduler);
    }

    @Provides
    public MovieDetailsPresenter provideMovieDetailsPresenter (Scheduler mainScheduler,
                                                               MovieDetailsApiInteractor
                                                                       apiInteractor) {
        return new MovieDetailsPresenterImpl (apiInteractor, mainScheduler);
    }

}
