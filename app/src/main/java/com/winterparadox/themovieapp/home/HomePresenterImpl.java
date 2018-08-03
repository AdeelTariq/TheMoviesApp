package com.winterparadox.themovieapp.home;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public class HomePresenterImpl extends HomePresenter {

    private HomeApiInteractor api;
    private Scheduler mainScheduler;
    private Disposable popularDisposable;
    private Disposable latestDisposable;

    public HomePresenterImpl (HomeApiInteractor api, Scheduler mainScheduler) {

        this.api = api;
        this.mainScheduler = mainScheduler;
    }

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

        if ( latestDisposable != null && !latestDisposable.isDisposed () ) {
            latestDisposable.dispose ();
        }

        popularDisposable = api.popularMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (view.popularTitle ());
                        objects.addAll (movies.subList (0, 4));
                        view.showMovies (objects);
                        view.hideProgress ();
                    }
                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                });

        latestDisposable = api.latestMovies ()
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        ArrayList<Object> objects = new ArrayList<> ();
                        objects.add (view.latestTitle ());
                        objects.addAll (movies.subList (0, 4));
                        view.showMovies (objects);
                        view.hideProgress ();
                    }
                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                });
    }
}
