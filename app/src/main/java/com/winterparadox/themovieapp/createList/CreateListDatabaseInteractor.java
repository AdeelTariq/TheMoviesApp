package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

public interface CreateListDatabaseInteractor {

    Long createList (UserList list);

    Long addToList (UserListItem listItem);
}
