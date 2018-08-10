package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class ChartMovieListPresenter extends BasePresenter<ChartMovieListView> {

    protected Chart chart;

    public void attachView (ChartMovieListView view, Chart chart, Navigator navigator) {
        this.chart = chart;
        super.attachView (view, navigator);
    }

    public abstract void fetchData ();

    public abstract void fetchDataPage (int page);

    public abstract void onMovieClicked (Movie movie, Object element);
}
