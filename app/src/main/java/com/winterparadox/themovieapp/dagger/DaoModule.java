package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.ChartDao;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;
import com.winterparadox.themovieapp.common.room.UserListDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adeel on 8/8/2017.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    public ChartDao providesChartDao (AppDatabase database) {
        return database.chartDao ();
    }

    @Singleton
    @Provides
    public FavoriteDao providesFavDao (AppDatabase database) {
        return database.favoriteDao ();
    }

    @Singleton
    @Provides
    public MovieDao providesMovieDao (AppDatabase database) {
        return database.movieDao ();
    }

    @Singleton
    @Provides
    public RecentlyViewedDao providesRecentlyViewedDao (AppDatabase database) {
        return database.recentlyViewedDao ();
    }

    @Singleton
    @Provides
    public UserListDao providesUserListDao (AppDatabase database) {
        return database.userListDao ();
    }
}
