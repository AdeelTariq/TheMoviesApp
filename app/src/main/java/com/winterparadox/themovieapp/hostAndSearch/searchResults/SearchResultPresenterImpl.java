package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;

public class SearchResultPresenterImpl extends SearchResultPresenter {

    private static final String VISIBLE_ITEM = "visiblePos", ITEMS = "items";

    private SearchResultApiInteractor api;
    private Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public SearchResultPresenterImpl (SearchResultApiInteractor api,
                                      Scheduler mainScheduler) {
        this.api = api;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (SearchResultView view, String q, Navigator navigator) {
        boolean tryRestore = false;
        if ( q.equals (this.query) ) {
            tryRestore = true;
        }
        super.attachView (view, q, navigator);

        if ( tryRestore && savedState.containsKey (ITEMS) &&
                ((List) savedState.get (ITEMS)).size () > 1 ) {  // 1 to avoid restoring error view
            List items = ((List) savedState.get (ITEMS));
            int visiblePos = (int) savedState.get (VISIBLE_ITEM);
            int page = (items.size () / 20) - 1;
            if ( page < 0 ) page = 0;
            if ( view != null ) {
                view.restoreMovies (visiblePos, items, page);
            }
        }
    }

    @Override
    public void saveState (int visibleItemPosition, List<Object> items) {
        savedState.put (VISIBLE_ITEM, visibleItemPosition);
        savedState.put (ITEMS, items);
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchDataPage (int page) {

        view.showPageProgress ();

        api.search (query, page)
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            for ( Movie movie : movies ) {
                                movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
                            }
                            view.showMovies (movies);
                            view.hideProgress ();

                        } else {
                            for ( Movie movie : movies ) {
                                movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
                            }
                            view.hidePageProgress ();
                            view.addMovies (movies);
                        }
                    }

                }, throwable -> {
                    if ( view != null ) {
                        if ( page == 0 ) {
                            view.showError (throwable.getMessage ());
                            view.hideProgress ();

                        } else {
                            view.hidePageProgress ();
                            view.showMessage (throwable.getMessage ());
                        }
                    }
                    throwable.printStackTrace ();
                });

    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }

    @Override
    public void onRetryClicked () {
        if ( navigator != null ) {
            navigator.openSearch ();
        }
    }
}
