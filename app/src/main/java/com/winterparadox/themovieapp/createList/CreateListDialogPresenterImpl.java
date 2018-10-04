package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import io.reactivex.Completable;

public class CreateListDialogPresenterImpl extends CreateListDialogPresenter {

    private CreateListDatabaseInteractor database;

    public CreateListDialogPresenterImpl (CreateListDatabaseInteractor database) {

        this.database = database;
    }

    @Override
    public void createList (String name, long movieId) {
        if ( name.isEmpty () ) {
            return;
        }

        database.createList (new UserList (name))
                .flatMapCompletable (id -> {
                    if ( movieId == Movie.NONE ) {
                        return Completable.complete ();
                    }
                    return database.addToList (new UserListItem (id, movieId));
                }).subscribe ();
    }
}
