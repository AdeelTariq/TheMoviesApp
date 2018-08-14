package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.UserList;

public abstract class RenameListDialogPresenter extends BasePresenter<RenameListDialogView> {

    public abstract void renameList (String name, UserList userList);
}
