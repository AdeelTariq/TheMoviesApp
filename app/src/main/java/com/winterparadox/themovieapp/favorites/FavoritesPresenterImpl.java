package com.winterparadox.themovieapp.favorites;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.HashMap;

import io.reactivex.Scheduler;

public class FavoritesPresenterImpl extends FavoritesPresenter {

    private static final String VISIBLE_ITEM = "visibleItem";

    private final FavoritesDatabaseInteractor database;
    private final Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public FavoritesPresenterImpl (FavoritesDatabaseInteractor database,
                                   Scheduler mainScheduler) {
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (FavoritesView v, Navigator navigator) {
        super.attachView (v, navigator);

        view.showProgress ();

        database.getFavorites ()
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
                            for ( Movie movie : movies ) {
                                movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
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
    void unFavorite (Movie movie) {
        database.unFavorite (movie).subscribe ();
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }
}
