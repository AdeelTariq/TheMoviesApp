package com.winterparadox.themovieapp.userLists;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class UserListsPresenterImpl extends UserListsPresenter {

    private static final String VISIBLE_ITEM = "visibleItem";

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public UserListsPresenterImpl (AppDatabase database,
                                   Scheduler mainScheduler) {
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (UserListsView view, Navigator navigator) {
        super.attachView (view, navigator);

        loadData ();
    }

    @Override
    public void saveState (int lastVisibleItem) {
        savedState.put (VISIBLE_ITEM, lastVisibleItem);
    }

    @SuppressLint("CheckResult")
    private void loadData () {

        database.userListDao ()
                .anyExists ()
                .map (anyExists -> {
                    if ( !anyExists ) {
                        if ( view != null ) {
                            List<String> defaultLists = view.getDefaultLists ();
                            for ( String defaultList : defaultLists ) {
                                database.userListDao ().insert (new UserList (defaultList));
                            }
                        }
                    }
                    return anyExists;
                })
                .map (b -> database.userListDao ().getUserLists ().blockingGet ())
                .toObservable ()
                .flatMapIterable (userLists -> userLists)
                .map (userList -> {
                    try {
                        Movie movie = database.userListDao ()
                                .getTopListMovie (userList.id).blockingGet ();
                        userList.backDropPath = movie.backdropPath;
                    } catch ( Exception e ) {
                        e.printStackTrace ();
                    }
                    return userList;
                })
                .toList ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (userLists -> {
                    if ( view != null ) {
                        int lastVisibleItem = 0;
                        if ( savedState.containsKey (VISIBLE_ITEM) ) {
                            lastVisibleItem = (int) savedState.get (VISIBLE_ITEM);
                        }
                        view.showCharts (userLists, lastVisibleItem);
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                    }
                    throwable.printStackTrace ();
                });
    }

    @Override
    public void onUserListClicked (UserList list) {
        if ( navigator != null ) {
            navigator.openMyList (list);
        }
    }
}