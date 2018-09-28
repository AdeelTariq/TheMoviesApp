package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.common.beans.UserList;

import io.reactivex.Completable;

public interface RenameListDatabaseInteractor {

    Completable renameList (String name, UserList userList);
}
