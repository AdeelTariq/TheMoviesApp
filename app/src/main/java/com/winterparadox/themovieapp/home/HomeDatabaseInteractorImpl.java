package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class HomeDatabaseInteractorImpl implements HomeDatabaseInteractor {

    private FavoriteDao favoriteDao;
    private RecentlyViewedDao recentlyViewedDao;

    public HomeDatabaseInteractorImpl (FavoriteDao favoriteDao,
                                       RecentlyViewedDao recentlyViewedDao) {
        this.favoriteDao = favoriteDao;
        this.recentlyViewedDao = recentlyViewedDao;
    }

    @Override
    public Single<List<Movie>> getHomeFavorites () {
        return favoriteDao.getHomeFavorites (4).subscribeOn (Schedulers.io ());
    }

    @Override
    public Single<List<Movie>> getHomeRecents () {
        return recentlyViewedDao.getRecent (4).subscribeOn (Schedulers.io ());
    }
}
