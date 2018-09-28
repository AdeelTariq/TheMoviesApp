package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.common.beans.UserList;

public class RenameListDialogPresenterImpl extends RenameListDialogPresenter {

    private RenameListDatabaseInteractor database;

    public RenameListDialogPresenterImpl (RenameListDatabaseInteractor database) {

        this.database = database;
    }

    @Override
    public void renameList (String name, UserList userList) {
        if ( name.isEmpty () ) {
            return;
        }

        database.renameList (name, userList).subscribe ();
    }
}
