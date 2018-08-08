package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.room.AppDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_TOP_RATED;

public class HostPresenterImpl extends HostPresenter {

    private HostApiInteractor api;
    private final AppDatabase database;
    private final Scheduler mainScheduler;
    private Disposable favMenuDisposable;
    private Disposable recentMenuDisposable;
    private Disposable chartsDisposable;

    public HostPresenterImpl (HostApiInteractor api, AppDatabase database,
                              Scheduler mainScheduler) {
        this.api = api;
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (HostView view) {
        super.attachView (view);

        createCharts ();

        favMenuDisposable = database.favoriteDao ()
                .anyExists ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (anyExists -> {
                    if ( view != null ) {
                        view.showFavoritesMenu (anyExists);
                    }
                });

        recentMenuDisposable = database.recentlyViewedDao ()
                .anyExists ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (anyExists -> {
                    if ( view != null ) {
                        view.showRecentMenu (anyExists);
                    }
                });

    }

    @Override
    public void fetchChartData () {
        if ( chartsDisposable != null && !chartsDisposable.isDisposed () ) {
            chartsDisposable.dispose ();
        }

        createCharts ();
    }

    private void createCharts () {
        // get tmdb genres
        chartsDisposable = api.generes ()
                .map (charts -> {       // add gather charts and genres
                    if ( view != null ) {
                        List<String> defaultCharts = view.getDefaultCharts ();

                        for ( int i = defaultCharts.size () - 1; i >= 0; i-- ) {
                            String defaultChart = defaultCharts.get (i);
                            charts.add (0, new Chart (i, defaultChart));
                        }
                    }

                    return charts;

                }).toObservable ()
                .flatMapIterable (charts -> charts)
                .flatMapSingle (chart -> {          // save to database if not already saved
                    database.chartDao ().insert (chart);
                    return database.chartDao ().getChart (chart.id);
                })
                // check if any chart's backdrop image is missing
                .filter (chart -> chart.backDropPath == null)

                // download the backdrop image path
                .flatMapSingle (chart -> {
                    switch ( chart.id ) {
                        case CHART_POPULAR:
                            return api.popularMovieBackdrop (chart);
                        case CHART_LATEST:
                            return api.latestMovieBackdrop (chart);
                        case CHART_TOP_RATED:
                            return api.topRatedMovieBackdrop (chart);
                        default:
                            return api.genreMovieBackdrop (chart);
                    }

                })
                .delay (300, TimeUnit.MILLISECONDS) // delay to avoid api limit
                // and then save
                .map (chart -> database.chartDao ().update (chart))
                .subscribe (aLong -> {
                        },
                        Throwable::printStackTrace);

    }

    @Override
    public void detachView () {
        super.detachView ();
        if ( favMenuDisposable != null && !favMenuDisposable.isDisposed () ) {
            favMenuDisposable.dispose ();
        }
        if ( recentMenuDisposable != null && !recentMenuDisposable.isDisposed () ) {
            recentMenuDisposable.dispose ();
        }
    }
}
