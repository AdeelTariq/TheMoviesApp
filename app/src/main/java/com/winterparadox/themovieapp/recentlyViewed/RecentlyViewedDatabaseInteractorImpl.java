package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RecentlyViewedDatabaseInteractorImpl implements RecentlyViewedDatabaseInteractor {

    private RecentlyViewedDao recentlyViewedDao;

    public RecentlyViewedDatabaseInteractorImpl (RecentlyViewedDao recentlyViewedDao) {

        this.recentlyViewedDao = recentlyViewedDao;
    }

    @Override
    public Single<List<Movie>> getRecentlyViewed (int count) {
        return recentlyViewedDao.getRecent (count)
                .subscribeOn (Schedulers.io ());
    }
}
