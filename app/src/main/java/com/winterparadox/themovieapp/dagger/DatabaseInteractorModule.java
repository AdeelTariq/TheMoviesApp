package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.charts.ChartsDatabaseInteractor;
import com.winterparadox.themovieapp.charts.ChartsDatabaseInteractorImpl;
import com.winterparadox.themovieapp.common.room.ChartDao;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;
import com.winterparadox.themovieapp.common.room.UserListDao;
import com.winterparadox.themovieapp.createList.CreateListDatabaseInteractor;
import com.winterparadox.themovieapp.createList.CreateListDatabaseInteractorImpl;
import com.winterparadox.themovieapp.favorites.FavoritesDatabaseInteractor;
import com.winterparadox.themovieapp.favorites.FavoritesDatabaseInteractorImpl;
import com.winterparadox.themovieapp.home.HomeDatabaseInteractor;
import com.winterparadox.themovieapp.home.HomeDatabaseInteractorImpl;
import com.winterparadox.themovieapp.hostAndSearch.HostDatabaseInteractor;
import com.winterparadox.themovieapp.hostAndSearch.HostDatabaseInteractorImpl;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsDatabaseInteractor;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsDatabaseInteractorImpl;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogDatabaseInteractor;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogDatabaseInteractorImpl;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedDatabaseInteractor;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedDatabaseInteractorImpl;
import com.winterparadox.themovieapp.userLists.UserListsDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.UserListsDatabaseInteractorImpl;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDatabaseInteractorImpl;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListDatabaseInteractorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adeel on 10/1/2017.
 */
@Module
public class DatabaseInteractorModule {


    @Provides
    @Singleton
    public HomeDatabaseInteractor provideHomeDatabaseInteractor (RecentlyViewedDao recentDao,
                                                                 FavoriteDao favoriteDao) {
        return new HomeDatabaseInteractorImpl (favoriteDao, recentDao);
    }

    @Provides
    @Singleton
    public HostDatabaseInteractor provideHostDatabaseInteractor (RecentlyViewedDao recentDao,
                                                                 FavoriteDao favoriteDao,
                                                                 ChartDao chartDao, MovieDao
                                                                             movieDao) {
        return new HostDatabaseInteractorImpl (chartDao, movieDao, favoriteDao, recentDao);
    }

    @Provides
    @Singleton
    public ChartsDatabaseInteractor provideChartsDatabaseInteractor (ChartDao chartDao) {
        return new ChartsDatabaseInteractorImpl (chartDao);
    }

    @Provides
    @Singleton
    public CreateListDatabaseInteractor provideCreateListDatabaseInteractor (UserListDao dao) {
        return new CreateListDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public UserListDialogDatabaseInteractor provideUserListDialogDatabaseInteractor (UserListDao dao) {
        return new UserListDialogDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public FavoritesDatabaseInteractor provideFavoritesDatabaseInteractor (FavoriteDao dao) {
        return new FavoritesDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public MovieDetailsDatabaseInteractor provideMovieDetailsDatabaseInteractor (MovieDao dao,
                                                                                 RecentlyViewedDao recentDao,
                                                                                 FavoriteDao favDao,
                                                                                 UserListDao userListDao) {
        return new MovieDetailsDatabaseInteractorImpl (dao, recentDao, favDao, userListDao);
    }

    @Provides
    @Singleton
    public RecentlyViewedDatabaseInteractor provideRecentlyViewedDatabaseInteractor
            (RecentlyViewedDao dao) {
        return new RecentlyViewedDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public UserListsDatabaseInteractor provideUserListsDatabaseInteractor (UserListDao dao) {
        return new UserListsDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public UserMovieListDatabaseInteractor provideUserMovieListDatabaseInteractor (UserListDao dao) {
        return new UserMovieListDatabaseInteractorImpl (dao);
    }

    @Provides
    @Singleton
    public RenameListDatabaseInteractor provideRenameListDatabaseInteractor (UserListDao dao) {
        return new RenameListDatabaseInteractorImpl (dao);
    }

}
