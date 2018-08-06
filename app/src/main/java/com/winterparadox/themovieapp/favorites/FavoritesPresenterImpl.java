package com.winterparadox.themovieapp.favorites;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.room.AppDatabase;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class FavoritesPresenterImpl extends FavoritesPresenter {

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    public FavoritesPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (FavoritesView view) {
        super.attachView (view);

        database.favoriteDao ()
                .getFavorites ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe ((movies, throwable) -> {
                    if ( throwable != null ) {
                        throwable.printStackTrace ();
                        if ( view != null ) {
                            view.showError (throwable.getMessage ());
                        }
                    } else {
                        if ( view != null ) {
                            view.showMovies (movies);
                        }
                    }
                });
    }

    @Override
    public void unFavorite (Movie movie) {
        Completable.fromAction (() -> database.favoriteDao ()
                .deleteAll (new Favorite (0, movie)))
                .subscribeOn (Schedulers.io ()).subscribe ();
    }
}
