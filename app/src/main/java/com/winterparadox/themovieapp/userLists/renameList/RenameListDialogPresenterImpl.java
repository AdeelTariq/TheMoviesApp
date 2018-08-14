package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class RenameListDialogPresenterImpl extends RenameListDialogPresenter {

    private AppDatabase database;

    public RenameListDialogPresenterImpl (AppDatabase database) {

        this.database = database;
    }

    @Override
    public void renameList (String name, UserList userList) {
        if ( name.isEmpty () ) {
            return;
        }

        userList.name = name;

        Completable.fromAction (() -> database.userListDao ()
                .updateList (userList))
                .subscribeOn (Schedulers.io ())
                .subscribe ();
    }
}
