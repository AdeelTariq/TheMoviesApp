package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

public class CreateListDatabaseInteractorImpl implements CreateListDatabaseInteractor {

    private UserListDao userListDao;

    public CreateListDatabaseInteractorImpl (UserListDao userListDao) {

        this.userListDao = userListDao;
    }

    @Override
    public Long createList (UserList list) {
        return userListDao.insertList (list);
    }

    @Override
    public Long addToList (UserListItem listItem) {
        return userListDao.addToList (listItem);
    }
}
