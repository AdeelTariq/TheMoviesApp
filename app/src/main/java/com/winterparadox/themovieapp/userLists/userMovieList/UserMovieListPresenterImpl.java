package com.winterparadox.themovieapp.userLists.userMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class UserMovieListPresenterImpl extends UserMovieListPresenter {

    private static final String VISIBLE_ITEM = "visiblePos";

    private Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();
    private AppDatabase database;

    public UserMovieListPresenterImpl (AppDatabase database,
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
        database.userListDao ()
                .getListMovies (userList.id)
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
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
        Completable.fromAction (() -> database.userListDao ()
                .removeFromList (new UserListItem (userList.id, movie.id)))
                .subscribeOn (Schedulers.io ())
                .subscribe ();
    }

    @Override
    public void saveListOrder (List<Object> movies) {
        Completable.fromAction (() -> {
            for ( int i = 0, moviesSize = movies.size (); i < moviesSize; i++ ) {
                Object movie = movies.get (i);
                if ( !(movie instanceof Movie) ) {
                    continue;
                }
                UserListItem listItem = new UserListItem (userList.id, ((Movie) movie).id);
                listItem.order = i;
                database.userListDao ().addToList (listItem);
            }
        }).subscribeOn (Schedulers.io ()).subscribe ();
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
