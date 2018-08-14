package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class CreateListDialogPresenterImpl extends CreateListDialogPresenter {

    private AppDatabase database;

    public CreateListDialogPresenterImpl (AppDatabase database) {

        this.database = database;
    }

    @Override
    public void createList (String name, long movieId) {
        if ( name.isEmpty () ) {
            return;
        }

        Completable.fromAction (() -> {
            long id = database.userListDao ().insertList (new UserList (name));

            if ( movieId != Movie.NONE ) {
                database.userListDao ().addToList (new UserListItem (id, movieId));
            }
        }).subscribeOn (Schedulers.io ())
                .subscribe ();
    }
}
