package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.common.beans.UserList;

import io.reactivex.Completable;

public interface UserListDialogDatabaseInteractor {

    Completable addToList (UserList list, long movieId);

    Completable removeFromList (UserList list, long movieId);
}
