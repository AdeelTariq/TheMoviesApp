package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class UserListDialogPresenterImpl extends UserListDialogPresenter {

    private AppDatabase database;

    public UserListDialogPresenterImpl (AppDatabase database) {

        this.database = database;
    }

    @Override
    public void addToList (long movieId, UserList list) {
        Completable.fromAction (() -> database.userListDao ()
                .addToList (new UserListItem (list.id, movieId)))
                .subscribeOn (Schedulers.io ()).subscribe ();
    }

    @Override
    public void removeFromList (long movieId, UserList list) {
        Completable.fromAction (() -> database.userListDao ()
                .removeFromList (new UserListItem (list.id, movieId)))
                .subscribeOn (Schedulers.io ()).subscribe ();
    }

    @Override
    public void onCreateNewClicked (long movieId) {
        navigator.openCreateList (movieId);
    }
}
