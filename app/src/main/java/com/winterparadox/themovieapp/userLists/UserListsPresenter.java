package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.UserList;

public abstract class UserListsPresenter extends BasePresenter<UserListsView> {

    public abstract void saveState (int lastVisibleItem);

    public abstract void onUserListClicked (UserList list);

    public abstract void onAddClicked ();

    public abstract void duplicateList (UserList list);

    public abstract void deleteList (UserList list);

    public abstract void onRenameClicked (UserList list);
}
