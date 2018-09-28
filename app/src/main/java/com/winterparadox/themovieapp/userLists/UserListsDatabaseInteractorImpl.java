package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.UserListDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserListsDatabaseInteractorImpl implements UserListsDatabaseInteractor {
    private UserListDao userListDao;

    public UserListsDatabaseInteractorImpl (UserListDao userListDao) {
        this.userListDao = userListDao;
    }

    @Override
    public Single<Boolean> anyUserListExists () {
        return userListDao.anyListExists ().subscribeOn (Schedulers.io ());
    }

    @Override
    public void createDefaultUserLists (List<String> defaultLists) {
        for ( String defaultList : defaultLists ) {
            userListDao.insertList (new UserList (defaultList));
        }
    }

    @Override
    public Flowable<List<UserList>> getUserLists () {
        return userListDao.getUserLists ().subscribeOn (Schedulers.io ());
    }

    @Override
    public boolean isListEmpty (UserList list) {
        return !userListDao.anyItemExists (list.id);
    }

    @Override
    public Movie getTopMovieFromList (UserList list) {
        return userListDao.getTopListMovie (list.id)
                .subscribeOn (Schedulers.io ())
                .blockingGet ();
    }

    @Override
    public Completable duplicateListItems (UserList list) {
        return Completable.fromAction (() -> userListDao
                .duplicateListItems (list))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable deleteList (UserList list) {
        return Completable.fromAction (() -> {
            userListDao.deleteListItems (list.id);
            userListDao.deleteList (list);
        }).subscribeOn (Schedulers.io ());
    }
}
