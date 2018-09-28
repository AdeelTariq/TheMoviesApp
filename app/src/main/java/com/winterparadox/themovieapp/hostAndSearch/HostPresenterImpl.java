package com.winterparadox.themovieapp.hostAndSearch;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_TOP_RATED;

public class HostPresenterImpl extends HostPresenter {

    private HostApiInteractor api;
    private final HostDatabaseInteractor database;
    private final Scheduler mainScheduler;
    private Disposable favMenuDisposable;
    private Disposable recentMenuDisposable;
    private Disposable chartsDisposable;

    public HostPresenterImpl (HostApiInteractor api,
                              HostDatabaseInteractor database,
                              Scheduler mainScheduler) {
        this.api = api;
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (HostView view, Navigator navigator) {
        super.attachView (view, navigator);

        fetchChartData ();

        favMenuDisposable = database.anyFavoriteExists ()
                .observeOn (mainScheduler)
                .subscribe (anyExists -> {
                    if ( view != null ) {
                        view.showFavoritesMenu (anyExists);
                    }
                });

        recentMenuDisposable = database.anyRecentyViewedExists ()
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

        if ( view != null ) {
            List<String> defaultCharts = view.getDefaultCharts ();
            chartsDisposable = PresenterUtils.createCharts (api.generes (),
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

                    }, database, defaultCharts);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getSuggestions (String query) {
        if ( query.length () < 3 ) {
            view.clearSuggestions ();
            return;
        }

        database.getSuggestios (query)
                .map (movies -> {
                    Collections.sort (movies,
                            (o1, o2) -> Integer.compare (o1.title.indexOf (query),
                                    o2.title.indexOf (query)));
                    return movies;
                })
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        view.showSuggestions (movies);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void search (String query) {
        if ( query.isEmpty () ) {
            return;
        }
        view.showProgress ();
        api.search (query)
                .observeOn (mainScheduler)
                .subscribe ((movies, throwable) -> {
                    if ( throwable != null ) {
                        throwable.printStackTrace ();
                        if ( view != null ) {
                            view.showError (throwable.getMessage ());
                            view.hideProgress ();
                        }
                    } else {
                        if ( view != null ) {
                            view.hideProgress ();
                        }
                        if ( navigator != null ) {
                            for ( Movie movie : movies ) {
                                movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
                            }
                            navigator.openSearchResults (query, movies);
                        }
                    }
                });
    }

    @Override
    public void onRecentlyViewedClicked () {
        if ( navigator != null ) {
            navigator.openRecentlyViewed ();
        }
    }

    @Override
    public void onFavoritesClicked () {
        if ( navigator != null ) {
            navigator.openFavorites ();
        }
    }

    @Override
    public void onMyListsClicked () {
        if ( navigator != null ) {
            navigator.openMyLists ();
        }
    }

    @Override
    public void onChartsClicked () {
        if ( navigator != null ) {
            navigator.openCharts ();
        }
    }

    @Override
    public void onMovieSuggestionClicked (Movie movie) {
        if ( navigator != null ) {
            navigator.openMovie (movie, null);
        }
    }

    @Override
    public void onAboutClicked () {
        if ( navigator != null ) {
            navigator.openAbout ();
        }
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
