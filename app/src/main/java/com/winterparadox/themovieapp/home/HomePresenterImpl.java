package com.winterparadox.themovieapp.home;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import io.reactivex.Scheduler;

public class HomePresenterImpl extends HomePresenter {

    private HomeApiInteractor api;
    private Scheduler mainScheduler;

    public HomePresenterImpl (HomeApiInteractor api, Scheduler mainScheduler) {

        this.api = api;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData () {
        view.showProgress ();
        api.popularMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        view.hideProgress ();
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (view.popularTitle ());
                        objects.addAll (movies.subList (0, 4));
                        view.showMovies (objects);
                    }
                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                    }
                });

        api.latestMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        view.hideProgress ();
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (view.latestTitle ());
                        objects.addAll (movies.subList (0, 4));
                        view.showMovies (objects);
                    }
                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                    }
                });
    }
}
