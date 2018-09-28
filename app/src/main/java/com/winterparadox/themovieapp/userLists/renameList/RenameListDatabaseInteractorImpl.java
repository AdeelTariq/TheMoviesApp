package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.UserListDao;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class RenameListDatabaseInteractorImpl implements RenameListDatabaseInteractor {

    private UserListDao userListDao;

    public RenameListDatabaseInteractorImpl (UserListDao userListDao) {
        this.userListDao = userListDao;
    }

    @Override
    public Completable renameList (String name, UserList userList) {
        userList.name = name;
        return Completable.fromAction (() -> userListDao.updateList (userList))
                .subscribeOn (Schedulers.io ());
    }
}
