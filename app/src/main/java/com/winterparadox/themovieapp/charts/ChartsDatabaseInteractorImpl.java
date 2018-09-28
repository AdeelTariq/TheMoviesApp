package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.room.ChartDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ChartsDatabaseInteractorImpl implements ChartsDatabaseInteractor {

    private ChartDao chartDao;

    public ChartsDatabaseInteractorImpl (ChartDao chartDao) {
        this.chartDao = chartDao;
    }

    @Override
    public Flowable<List<Chart>> getCharts () {
        return chartDao.getCharts ().subscribeOn (Schedulers.io ());
    }

    @Override
    public Long insert (Chart chart) {
        return chartDao.insert (chart);
    }

    @Override
    public Long update (Chart chart) {
        return chartDao.update (chart);
    }

    @Override
    public Single<Chart> getChart (long id) {
        return chartDao.getChart (id).subscribeOn (Schedulers.io ());
    }
}
