package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.ChartDao;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class HostDatabaseInteractorImpl implements HostDatabaseInteractor {

    private ChartDao chartDao;
    private MovieDao movieDao;
    private FavoriteDao favoriteDao;
    private RecentlyViewedDao recentlyViewedDao;

    public HostDatabaseInteractorImpl (ChartDao chartDao, MovieDao movieDao,
                                       FavoriteDao favoriteDao,
                                       RecentlyViewedDao recentlyViewedDao) {
        this.chartDao = chartDao;
        this.movieDao = movieDao;
        this.favoriteDao = favoriteDao;
        this.recentlyViewedDao = recentlyViewedDao;
    }

    @Override
    public Long insertChart (Chart chart) {
        return chartDao.insert (chart);
    }

    @Override
    public Long updateChart (Chart chart) {
        return chartDao.update (chart);
    }

    @Override
    public Single<Chart> getChart (long id) {
        return chartDao.getChart (id).subscribeOn (Schedulers.io ());
    }

    @Override
    public Single<List<Movie>> getSuggestions (String query) {
        return movieDao.search ("%" + query + "%", 10)
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Flowable<Boolean> anyFavoriteExists () {
        return favoriteDao.anyExists ().subscribeOn (Schedulers.io ());
    }

    @Override
    public Flowable<Boolean> anyRecentyViewedExists () {
        return recentlyViewedDao.anyExists ().subscribeOn (Schedulers.io ());
    }
}
