package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.common.base.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Chart;

public abstract class ChartMovieListPresenter extends BasePresenter<ChartMovieListView> {

    protected Chart chart;

    public void attachView (ChartMovieListView view, Chart chart) {
        this.chart = chart;
        super.attachView (view);
    }

    public abstract void fetchData ();
}
