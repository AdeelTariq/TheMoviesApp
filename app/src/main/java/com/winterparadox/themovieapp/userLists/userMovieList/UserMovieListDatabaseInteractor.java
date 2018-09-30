package com.winterparadox.themovieapp.userLists.userMovieList;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface UserMovieListDatabaseInteractor {

    Single<List<Movie>> getListMovies (UserList list);

    Completable deleteFromList (UserList userList, Movie movie);

    Completable saveListOrder (UserList userList, List<Movie> movies);
}
