package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ChartsDatabaseInteractor {

    Flowable<List<Chart>> getCharts ();

    Long insert (Chart chart);

    Long update (Chart chart);

    Single<Chart> getChart (long id);
}
