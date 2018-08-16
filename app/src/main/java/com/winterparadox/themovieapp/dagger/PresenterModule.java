package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.charts.ChartsApiInteractor;
import com.winterparadox.themovieapp.charts.ChartsPresenter;
import com.winterparadox.themovieapp.charts.ChartsPresenterImpl;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListApiInteractor;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListPresenter;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListPresenterImpl;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.createList.CreateListDialogPresenter;
import com.winterparadox.themovieapp.createList.CreateListDialogPresenterImpl;
import com.winterparadox.themovieapp.favorites.FavoritesPresenter;
import com.winterparadox.themovieapp.favorites.FavoritesPresenterImpl;
import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomePresenter;
import com.winterparadox.themovieapp.home.HomePresenterImpl;
import com.winterparadox.themovieapp.hostAndSearch.HostApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.HostPresenter;
import com.winterparadox.themovieapp.hostAndSearch.HostPresenterImpl;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultPresenter;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultPresenterImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsApiInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenter;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsPresenterImpl;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogPresenter;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogPresenterImpl;
import com.winterparadox.themovieapp.personDetails.PersonApiInteractor;
import com.winterparadox.themovieapp.personDetails.PersonDetailsPresenter;
import com.winterparadox.themovieapp.personDetails.PersonDetailsPresenterImpl;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedPresenter;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedPresenterImpl;
import com.winterparadox.themovieapp.userLists.UserListsPresenter;
import com.winterparadox.themovieapp.userLists.UserListsPresenterImpl;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogPresenter;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogPresenterImpl;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListPresenter;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListPresenterImpl;

import javax.inject.Singleton;

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

    @Singleton
    @Provides
    public RecentlyViewedPresenter provideRecentlyViewedPresenter (AppDatabase database,
                                                                   Scheduler mainScheduler) {
        return new RecentlyViewedPresenterImpl (database, mainScheduler);
    }

    @Singleton
    @Provides
    public FavoritesPresenter provideFavoritePresenter (AppDatabase database,
                                                        Scheduler mainScheduler) {
        return new FavoritesPresenterImpl (database, mainScheduler);
    }

    @Provides
    public HostPresenter provideHostPresenter (HostApiInteractor api,
                                               AppDatabase database,
                                               Scheduler mainScheduler) {
        return new HostPresenterImpl (api, database, mainScheduler);
    }


    @Provides
    public PersonDetailsPresenter providePersonDetailsPresenter (PersonApiInteractor api,
                                                                 Scheduler mainScheduler) {
        return new PersonDetailsPresenterImpl (api, mainScheduler);
    }

    @Singleton
    @Provides
    public ChartsPresenter provideChartsPresenter (ChartsApiInteractor api,
                                                   AppDatabase appDatabase,
                                                   Scheduler mainScheduler) {
        return new ChartsPresenterImpl (api, appDatabase, mainScheduler);
    }

    @Provides
    @Singleton
    public ChartMovieListPresenter provideChartListPresenter (ChartMovieListApiInteractor
                                                                          interactor,
                                                              Scheduler mainScheduler) {
        return new ChartMovieListPresenterImpl (interactor, mainScheduler);
    }

    @Provides
    @Singleton
    public UserListsPresenter provideUserListPresenter (AppDatabase database,
                                                        Scheduler mainScheduler) {
        return new UserListsPresenterImpl (database, mainScheduler);
    }

    @Provides
    public UserListDialogPresenter provideUserListDialogPresenter (AppDatabase database) {
        return new UserListDialogPresenterImpl (database);
    }

    @Provides
    @Singleton
    public UserMovieListPresenter provideUserMovieListPresenter (AppDatabase database,
                                                                 Scheduler mainScheduler) {
        return new UserMovieListPresenterImpl (database, mainScheduler);
    }

    @Provides
    public CreateListDialogPresenter provideCreateListPresenter (AppDatabase database) {
        return new CreateListDialogPresenterImpl (database);
    }

    @Provides
    public RenameListDialogPresenter provideRenameListPresenter (AppDatabase database) {
        return new RenameListDialogPresenterImpl (database);
    }

    @Provides
    public SearchResultPresenter provideSearchResultPresenter (SearchResultApiInteractor interactor,
                                                               Scheduler scheduler) {
        return new SearchResultPresenterImpl (interactor, scheduler);
    }

}
