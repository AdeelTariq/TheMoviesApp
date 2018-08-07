package com.winterparadox.themovieapp.home;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.common.beans.HomeSection;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.room.AppDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_FAVORITES;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_POPULAR;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_RECENT;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_UPCOMING;

public class HomePresenterImpl extends HomePresenter {

    private HomeApiInteractor api;
    private AppDatabase database;
    private Scheduler mainScheduler;
    private Disposable popularDisposable;
    private Disposable upcomingDisposable;


    public HomePresenterImpl (HomeApiInteractor api, AppDatabase database, Scheduler
            mainScheduler) {

        this.api = api;
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData () {
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

        database.recentlyViewedDao ()
                .getRecent (4)
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( movies.isEmpty () ) {
                        return;
                    }
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (new HomeSection (SECTION_RECENT, view.recentlyTitle ()));
                        objects.addAll (movies);
                        view.showMovies (objects);
                    }
                });

        database.favoriteDao ()
                .getHomeFavorites (4)
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( movies.isEmpty () ) {
                        return;
                    }
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (new HomeSection (SECTION_FAVORITES, view.favoriteTitle ()));
                        objects.addAll (movies);
                        view.showMovies (objects);
                    }
                });

        popularDisposable = api.popularMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (new HomeSection (SECTION_POPULAR, view.popularTitle ()));
                        objects.addAll (movies.subList (0, 4));
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

        upcomingDisposable = api.upcomingMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (new HomeSection (SECTION_UPCOMING, view.upcomingTitle ()));
                        List<Movie> subList = movies.subList (0, 4);
                        Collections.sort (subList,
                                (t, t1) -> Double.compare (t.voteAverage, t1.voteAverage));
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
}
