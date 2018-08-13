package com.winterparadox.themovieapp.charts.chartMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_TOP_RATED;

public class ChartMovieListPresenterImpl extends ChartMovieListPresenter {

    private static final String VISIBLE_ITEM = "visiblePos", ITEMS = "items";

    private ChartMovieListApiInteractor api;
    private Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public ChartMovieListPresenterImpl (ChartMovieListApiInteractor api,
                                        Scheduler mainScheduler) {
        this.api = api;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (ChartMovieListView view, Chart chart, Navigator navigator) {
        boolean tryRestore = false;
        if ( chart == this.chart ) {
            tryRestore = true;
        }
        super.attachView (view, chart, navigator);

        if ( tryRestore && savedState.containsKey (ITEMS) &&
                ((List) savedState.get (ITEMS)).size () > 1 ) {  // 1 to avoid restoring error view
            List items = ((List) savedState.get (ITEMS));
            int visiblePos = (int) savedState.get (VISIBLE_ITEM);
            int page = (items.size () / 20) - 1;

            if ( view != null ) {
                view.restoreMovies (visiblePos, items, page);
            }
        } else {
            fetchData ();
        }
    }

    @Override
    public void saveState (int visibleItemPosition, List<Object> items) {
        savedState.put (VISIBLE_ITEM, visibleItemPosition);
        savedState.put (ITEMS, items);
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

        if ( chart.id == CHART_POPULAR ) {
            movieSingle = api.popularMovies (page);

        } else if ( chart.id == CHART_LATEST ) {
            movieSingle = api.latestMovies (page);

        } else if ( chart.id == CHART_TOP_RATED ) {
            movieSingle = api.topRatedMovies (page);

        } else {
            movieSingle = api.topRatedInGenre (chart.id, page);

        }

        movieSingle
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            view.showMovies (movies);
                            view.hideProgress ();

                        } else {
                            view.hidePageProgress ();
                            view.addMovies (movies);
                        }
                    }

                }, throwable -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            view.showError (throwable.getMessage ());
                            view.hideProgress ();

                        } else {
                            view.hidePageProgress ();
                            view.showMessage (throwable.getMessage ());
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
