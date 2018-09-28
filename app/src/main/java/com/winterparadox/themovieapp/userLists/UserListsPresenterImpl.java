package com.winterparadox.themovieapp.userLists;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class UserListsPresenterImpl extends UserListsPresenter {

    private static final String VISIBLE_ITEM = "visibleItem";

    private final UserListsDatabaseInteractor database;
    private final Scheduler mainScheduler;

    private HashMap<String, Object> savedState = new HashMap<> ();

    public UserListsPresenterImpl (UserListsDatabaseInteractor database,
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

        database.anyUserListExists ()
                .map (anyExists -> {
                    if ( !anyExists ) {
                        if ( view != null ) {
                            List<String> defaultLists = view.getDefaultLists ();
                            database.createDefaultUserLists (defaultLists);
                        }
                    }
                    return anyExists;
                })
                .flatMapPublisher (b -> database.getUserLists ())
                .map (userLists -> {
                    for ( UserList userList : userLists ) {
                        if ( !database.isListEmpty (userList) ) {
                            Movie movie = database.getTopMovieFromList (userList);
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
        database.duplicateListItems (list).subscribe ();
    }

    @Override
    public void deleteList (UserList list) {
        database.deleteList (list).subscribe ();
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
