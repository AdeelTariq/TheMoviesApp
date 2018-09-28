package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface UserListsDatabaseInteractor {


    Single<Boolean> anyUserListExists ();

    void createDefaultUserLists (List<String> defaultLists);

    Flowable<List<UserList>> getUserLists ();

    boolean isListEmpty (UserList list);

    Movie getTopMovieFromList (UserList list);

    Completable duplicateListItems (UserList list);

    Completable deleteList (UserList list);

}
