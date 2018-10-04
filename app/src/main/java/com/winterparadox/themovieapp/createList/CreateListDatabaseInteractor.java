package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface CreateListDatabaseInteractor {

    Single<Long> createList (UserList list);

    Completable addToList (UserListItem listItem);
}
