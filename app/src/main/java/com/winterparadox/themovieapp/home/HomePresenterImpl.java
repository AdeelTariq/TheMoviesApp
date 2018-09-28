package com.winterparadox.themovieapp.home;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.HomeSection;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_FAVORITES;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_POPULAR;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_RECENT;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_UPCOMING;

public class HomePresenterImpl extends HomePresenter {

    private HomeApiInteractor api;
    private HomeDatabaseInteractor database;
    private Scheduler mainScheduler;
    private Disposable popularDisposable;
    private Disposable upcomingDisposable;


    public HomePresenterImpl (HomeApiInteractor api, HomeDatabaseInteractor database,
                              Scheduler mainScheduler) {

        this.api = api;
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    void fetchData () {
        if ( view == null ) {
            return;
        }
        view.showProgress ();
        view.clearView ();

        if ( popularDisposable != null && !popularDisposable.isDisposed () ) {
            popularDisposable.dispose ();
        }

        if ( upcomingDisposable != null && !upcomingDisposable.isDisposed () ) {
            upcomingDisposable.dispose ();
        }

        loadMovies (database.getHomeRecents (),
                SECTION_RECENT, view.recentlyTitle ());

        loadMovies (database.getHomeFavorites (),
                SECTION_FAVORITES, view.favoriteTitle ());

        popularDisposable = loadMovies (api.popularMovies (),
                SECTION_POPULAR, view.popularTitle ());

        upcomingDisposable = loadMovies (api.upcomingMovies (),
                SECTION_UPCOMING, view.upcomingTitle ());
    }

    @NonNull
    private Disposable loadMovies (Single<List<Movie>> listSingle, int sectionPopular,
                                   String title) {
        return listSingle
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {

                        if ( movies.size () < 4 ) {
                            return;
                        }

                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (new HomeSection (sectionPopular, title));
                        List<Movie> subList = movies;
                        if ( movies.size () > 4 ) {
                            subList = movies.subList (0, 4);
                            Collections.sort (subList, (t, t1) -> Double.compare (t.voteAverage,
                                    t1.voteAverage));
                        }
                        objects.addAll (subList);
                        view.showMovies (objects);
                        view.hideProgress ();
                    }
                }, throwable -> {
                    throwable.printStackTrace ();
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                });
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }

    @Override
    public void onChartClicked (Chart chart) {
        if ( navigator != null ) {
            navigator.openChartMovieList (chart);
        }
    }

    @Override
    public void onFavoritesClicked () {
        if ( navigator != null ) {
            navigator.openFavorites ();
        }
    }

    @Override
    public void onRecentlyViewedClicked () {
        if ( navigator != null ) {
            navigator.openRecentlyViewed ();
        }
    }
}
