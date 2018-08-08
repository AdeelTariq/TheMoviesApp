package com.winterparadox.themovieapp.charts.chartMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;

public class ChartMovieListPresenterImpl extends ChartMovieListPresenter {

    private ChartMovieListApiInteractor api;
    private Scheduler mainScheduler;

    public ChartMovieListPresenterImpl (ChartMovieListApiInteractor api,
                                        Scheduler mainScheduler) {
        this.api = api;

        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (ChartMovieListView view, Chart chart) {
        super.attachView (view, chart);

        fetchData ();
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData () {

        view.showProgress ();

        Single<List<Movie>> movieSingle;

        switch ( chart.id ) {
            case CHART_POPULAR:
                movieSingle = api.popularMovies ();
                break;
            case CHART_LATEST:
                movieSingle = api.latestMovies ();
                break;
            default:
                movieSingle = api.topRatedMovies ();
                break;
        }

        movieSingle
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        view.showMovies (movies);
                        view.hideProgress ();
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                    throwable.printStackTrace ();
                });

    }
}
