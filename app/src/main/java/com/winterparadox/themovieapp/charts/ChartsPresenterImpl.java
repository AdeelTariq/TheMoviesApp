package com.winterparadox.themovieapp.charts;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_TOP_RATED;

public class ChartsPresenterImpl extends ChartsPresenter {

    private static final String VISIBLE_ITEM = "visibleItem";

    private final ChartsDatabaseInteractor database;
    private final Scheduler mainScheduler;
    private Disposable chartsDisposable;
    private ChartsApiInteractor api;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public ChartsPresenterImpl (ChartsApiInteractor api,
                                ChartsDatabaseInteractor database,
                                Scheduler mainScheduler) {
        this.api = api;
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (ChartsView view, Navigator navigator) {
        super.attachView (view, navigator);

        loadData ();
    }

    @Override
    public void saveState (int lastVisibleItem) {
        savedState.put (VISIBLE_ITEM, lastVisibleItem);
    }

    @SuppressLint("CheckResult")
    private void loadData () {

        database.getCharts ()
                .observeOn (mainScheduler)
                .subscribe (charts -> {
                    if ( view != null ) {
                        int lastVisibleItem = 0;
                        if ( savedState.containsKey (VISIBLE_ITEM) ) {
                            lastVisibleItem = (int) savedState.get (VISIBLE_ITEM);
                        }
                        view.showCharts (charts, lastVisibleItem);
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                    }
                    throwable.printStackTrace ();
                });

    }

    @Override
    public void onChartClicked (Chart chart) {
        if ( navigator != null ) {
            navigator.openChartMovieList (chart);
        }
    }

    @Override
    void fetchChartData () {
        if ( chartsDisposable != null && !chartsDisposable.isDisposed () ) {
            chartsDisposable.dispose ();
        }

        if ( view != null ) {
            List<String> defaultCharts = view.getDefaultCharts ();
            chartsDisposable = PresenterUtils.createCharts (api.genres (),
                    chart -> {
                        database.insert (chart);
                        return database.getChart (chart.id);
                    },
                    chart -> {
                        if ( chart.id == CHART_POPULAR ) {
                            return api.popularMovieBackdrop (chart);
                        } else if ( chart.id == CHART_LATEST ) {
                            return api.latestMovieBackdrop (chart);
                        } else if ( chart.id == CHART_TOP_RATED ) {
                            return api.topRatedMovieBackdrop (chart);
                        } else {
                            return api.genreMovieBackdrop (chart);
                        }

                    }, chart -> {
                        Long id = database.update (chart);
                        return Single.just (id);
                    }, defaultCharts);
        }
    }

    @Override
    public void detachView () {
        super.detachView ();
        if ( chartsDisposable != null && !chartsDisposable.isDisposed () ) {
            chartsDisposable.dispose ();
        }
    }
}
