package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.room.AppDatabase;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HostPresenterImpl extends HostPresenter {

    private final AppDatabase database;
    private final Scheduler mainScheduler;
    private Disposable favMenuDisposable;
    private Disposable recentMenuDisposable;

    public HostPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (HostView view) {
        super.attachView (view);

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
