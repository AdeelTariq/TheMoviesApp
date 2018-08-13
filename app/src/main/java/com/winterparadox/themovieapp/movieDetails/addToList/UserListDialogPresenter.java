package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.UserList;

public abstract class UserListDialogPresenter extends BasePresenter<UserListDialogView> {


    public abstract void addToList (long movieId, UserList list);

    public abstract void removeFromList (long movieId, UserList list);

    public abstract void onCreateNewClicked (long movieId);
}
