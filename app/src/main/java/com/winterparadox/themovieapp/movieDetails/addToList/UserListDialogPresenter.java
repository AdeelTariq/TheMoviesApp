package com.winterparadox.themovieapp.movieDetails.addToList;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.UserList;

public abstract class UserListDialogPresenter extends BasePresenter<UserListDialogView> {


    public abstract void addToList (int movieId, UserList list);

    public abstract void removeFromList (int movieId, UserList list);

}
