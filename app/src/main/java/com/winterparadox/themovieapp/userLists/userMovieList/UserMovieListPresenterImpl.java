package com.winterparadox.themovieapp.userLists.userMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;

public class UserMovieListPresenterImpl extends UserMovieListPresenter {

    private static final String VISIBLE_ITEM = "visiblePos";

    private Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();
    private UserMovieListDatabaseInteractor database;

    public UserMovieListPresenterImpl (UserMovieListDatabaseInteractor database,
                                       Scheduler mainScheduler) {
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (UserMovieListView view, UserList list, Navigator navigator) {
        super.attachView (view, list, navigator);

        fetchData ();
    }

    @Override
    public void saveState (int visibleItemPosition) {
        savedState.put (VISIBLE_ITEM, visibleItemPosition);
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData () {
        database.getListMovies (userList)
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        for ( Movie movie : movies ) {
                            movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
                        }
                        view.showMovies (movies);
                        view.hideProgress ();
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                    throwable.printStackTrace ();
                });

    }

    @Override
    public void deleteMovie (Movie movie) {
        database.deleteFromList (userList, movie).subscribe ();
    }

    @Override
    public void saveListOrder (List<Object> movies) {
        database.saveListOrder (userList, movies).subscribe ();
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }

    @Override
    public void onDiscoverClick () {
        if ( navigator != null ) {
            navigator.openCharts ();
        }
    }
}
