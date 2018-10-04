package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class CreateListDatabaseInteractorImpl implements CreateListDatabaseInteractor {

    private UserListDao userListDao;

    public CreateListDatabaseInteractorImpl (UserListDao userListDao) {

        this.userListDao = userListDao;
    }

    @Override
    public Single<Long> createList (UserList list) {
        return Single.fromCallable (() -> userListDao.insertList (list))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable addToList (UserListItem listItem) {
        return Completable.fromAction (() -> userListDao.addToList (listItem));
    }
}
