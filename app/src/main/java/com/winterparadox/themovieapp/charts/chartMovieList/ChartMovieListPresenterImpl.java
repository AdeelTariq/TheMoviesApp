package com.winterparadox.themovieapp.charts.chartMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
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
    public void attachView (ChartMovieListView view, Chart chart, Navigator navigator) {
        super.attachView (view, chart, navigator);

        fetchData ();
    }

    @Override
    public void fetchData () {
        fetchDataPage (0);
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchDataPage (int page) {

        if ( page == 0 ) {
            view.showProgress ();

        } else {
            view.showPageProgress ();
        }

        Single<List<Movie>> movieSingle;

        switch ( chart.id ) {
            case CHART_POPULAR:
                movieSingle = api.popularMovies (page);
                break;
            case CHART_LATEST:
                movieSingle = api.latestMovies (page);
                break;
            default:
                movieSingle = api.topRatedMovies (page);
                break;
        }

        movieSingle
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            view.showMovies (movies);
                            view.hideProgress ();

                        } else {
                            view.addMovies (movies);
                            view.hidePageProgress ();
                        }
                    }

                }, throwable -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            view.showError (throwable.getMessage ());
                            view.hideProgress ();

                        } else {
                            view.showMessage (throwable.getMessage ());
                            view.hidePageProgress ();
                        }
                    }
                    throwable.printStackTrace ();
                });

    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }
}
