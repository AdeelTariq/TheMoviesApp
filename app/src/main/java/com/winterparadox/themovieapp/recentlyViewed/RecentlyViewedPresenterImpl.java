package com.winterparadox.themovieapp.recentlyViewed;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.room.AppDatabase;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RecentlyViewedPresenterImpl extends RecentlyViewedPresenter {

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    public RecentlyViewedPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (RecentlyViewedView view) {
        super.attachView (view);

        database.recentlyViewedDao ()
                .getRecent (200)
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
}
