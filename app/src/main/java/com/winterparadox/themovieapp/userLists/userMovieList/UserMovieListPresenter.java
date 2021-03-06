package com.winterparadox.themovieapp.userLists.userMovieList;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.List;

public abstract class UserMovieListPresenter extends BasePresenter<UserMovieListView> {

    protected UserList userList;

    public void attachView (UserMovieListView view, UserList list, Navigator navigator) {
        this.userList = list;
        super.attachView (view, navigator);
    }

    public abstract void fetchData ();

    public abstract void onMovieClicked (Movie movie, Object element);

    public abstract void onDiscoverClick ();

    public abstract void deleteMovie (Movie movie);

    public abstract void saveListOrder (List<Object> movies);
}
