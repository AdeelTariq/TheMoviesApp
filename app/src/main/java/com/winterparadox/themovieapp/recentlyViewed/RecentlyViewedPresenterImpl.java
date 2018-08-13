package com.winterparadox.themovieapp.recentlyViewed;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.room.AppDatabase;

import java.util.HashMap;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RecentlyViewedPresenterImpl extends RecentlyViewedPresenter {

    private static final String VISIBLE_ITEM = "visibleItem";

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public RecentlyViewedPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (RecentlyViewedView v, Navigator navigator) {
        super.attachView (v, navigator);

        view.showProgress ();

        database.recentlyViewedDao ()
                .getRecent (200)
                .subscribeOn (Schedulers.io ())
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
                            int lastVisibleItem = 0;
                            if ( savedState.containsKey (VISIBLE_ITEM) ) {
                                lastVisibleItem = (int) savedState.get (VISIBLE_ITEM);
                            }
                            view.showMovies (movies, lastVisibleItem);
                            view.hideProgress ();
                        }
                    }
                });
    }

    @Override
    public void saveState (int lastVisibleItem) {
        savedState.put (VISIBLE_ITEM, lastVisibleItem);
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }
}
