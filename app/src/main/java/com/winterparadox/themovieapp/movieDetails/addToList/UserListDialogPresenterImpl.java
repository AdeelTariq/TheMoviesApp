package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.common.beans.UserList;

public class UserListDialogPresenterImpl extends UserListDialogPresenter {

    private UserListDialogDatabaseInteractor database;

    public UserListDialogPresenterImpl (UserListDialogDatabaseInteractor database) {

        this.database = database;
    }

    @Override
    public void addToList (long movieId, UserList list) {
        database.addToList (list, movieId).subscribe ();
    }

    @Override
    public void removeFromList (long movieId, UserList list) {
        database.removeFromList (list, movieId).subscribe ();
    }

    @Override
    public void onCreateNewClicked (long movieId) {
        navigator.openCreateList (movieId);
    }
}
