package com.winterparadox.themovieapp.userLists;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
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
                .anyListExists ()
                .map (anyExists -> {
                    if ( !anyExists ) {
                        if ( view != null ) {
                            List<String> defaultLists = view.getDefaultLists ();
                            for ( String defaultList : defaultLists ) {
                                database.userListDao ().insertList (new UserList (defaultList));
                            }
                        }
                    }
                    return anyExists;
                })
                .flatMapPublisher (b -> database.userListDao ().getUserLists ())
                .map (userLists -> {
                    for ( UserList userList : userLists ) {
                        if ( database.userListDao ().anyItemExists (userList.id) ) {
                            Movie movie = database.userListDao ()
                                    .getTopListMovie (userList.id).blockingGet ();
                            userList.backDropPath = movie.backdropPath;
                        }
                    }
                    return userLists;
                })
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
    public void duplicateList (UserList list) {
        Completable.fromAction (() -> database.userListDao ()
                .duplicateListItems (list))
                .subscribeOn (Schedulers.io ()).subscribe ();
    }

    @Override
    public void deleteList (UserList list) {
        Completable.fromAction (() -> {
            database.userListDao ().deleteListItems (list.id);
            database.userListDao ().deleteList (list);
        }).subscribeOn (Schedulers.io ()).subscribe ();
    }

    @Override
    public void onUserListClicked (UserList list) {
        if ( navigator != null ) {
            navigator.openMyList (list);
        }
    }

    @Override
    public void onAddClicked () {
        if ( navigator != null ) {
            navigator.openCreateList (Movie.NONE);
        }
    }

    @Override
    public void onRenameClicked (UserList list) {
        if ( navigator != null ) {
            navigator.openRenameList (list);
        }
    }
}
