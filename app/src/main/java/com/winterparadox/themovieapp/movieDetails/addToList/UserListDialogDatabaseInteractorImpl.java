package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class UserListDialogDatabaseInteractorImpl implements UserListDialogDatabaseInteractor {

    private UserListDao userListDao;

    public UserListDialogDatabaseInteractorImpl (UserListDao userListDao) {

        this.userListDao = userListDao;
    }

    @Override
    public Completable addToList (UserList list, long movieId) {
        return Completable.fromAction (() -> userListDao
                .addToList (new UserListItem (list.id, movieId)))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable removeFromList (UserList list, long movieId) {
        return Completable.fromAction (() -> userListDao
                .removeFromList (new UserListItem (list.id, movieId)))
                .subscribeOn (Schedulers.io ());
    }
}
