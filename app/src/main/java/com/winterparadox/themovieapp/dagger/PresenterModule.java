package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.favorites.FavoritesPresenter;
import com.winterparadox.themovieapp.favorites.FavoritesPresenterImpl;
import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomePresenter;
import com.winterparadox.themovieapp.home.HomePresenterImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenter;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenterImpl;
import com.winterparadox.themovieapp.personDetails.PersonApiInteractor;
import com.winterparadox.themovieapp.personDetails.PersonDetailsPresenter;
import com.winterparadox.themovieapp.personDetails.PersonDetailsPresenterImpl;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedPresenter;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedPresenterImpl;
import com.winterparadox.themovieapp.room.AppDatabase;
import com.winterparadox.themovieapp.search.HostPresenter;
import com.winterparadox.themovieapp.search.HostPresenterImpl;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by Adeel on 10/1/2017.
 */

@Module
public class PresenterModule {

    @Provides
    public HomePresenter provideHomePresenter (HomeApiInteractor apiInteractor,
                                               AppDatabase database, Scheduler mainScheduler) {
        return new HomePresenterImpl (apiInteractor, database, mainScheduler);
    }

    @Provides
    public MovieDetailsPresenter provideMovieDetailsPresenter (Scheduler mainScheduler,
                                                               MovieDetailsApiInteractor
                                                                       apiInteractor,
                                                               AppDatabase database) {
        return new MovieDetailsPresenterImpl (apiInteractor, mainScheduler, database);
    }

    @Provides
    public RecentlyViewedPresenter provideRecentlyViewedPresenter (AppDatabase database,
                                                                   Scheduler mainScheduler) {
        return new RecentlyViewedPresenterImpl (database, mainScheduler);
    }

    @Provides
    public FavoritesPresenter provideFavoritePresenter (AppDatabase database,
                                                        Scheduler mainScheduler) {
        return new FavoritesPresenterImpl (database, mainScheduler);
    }

    @Provides
    public HostPresenter provideHostPresenter (AppDatabase database,
                                               Scheduler mainScheduler) {
        return new HostPresenterImpl (database, mainScheduler);
    }


    @Provides
    public PersonDetailsPresenter providePersonDetailsPresenter (PersonApiInteractor api,
                                                                 Scheduler mainScheduler) {
        return new PersonDetailsPresenterImpl (api, mainScheduler);
    }

}
