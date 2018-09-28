package com.winterparadox.themovieapp.userLists.userMovieList;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserMovieListDatabaseInteractorImpl implements UserMovieListDatabaseInteractor {

    private UserListDao userListDao;

    public UserMovieListDatabaseInteractorImpl (UserListDao userListDao) {
        this.userListDao = userListDao;
    }

    @Override
    public Single<List<Movie>> getListMovies (UserList list) {
        return userListDao.getListMovies (list.id).subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable deleteFromList (UserList userList, Movie movie) {
        return Completable.fromAction (() -> userListDao
                .removeFromList (new UserListItem (userList.id, movie.id)))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable saveListOrder (UserList userList, List<Object> movies) {
        return Completable.fromAction (() -> {
            for ( int i = 0, moviesSize = movies.size (); i < moviesSize; i++ ) {
                Object movie = movies.get (i);
                if ( !(movie instanceof Movie) ) {
                    continue;
                }
                UserListItem listItem = new UserListItem (userList.id, ((Movie) movie).id);
                listItem.order = i;
                userListDao.addToList (listItem);
            }
        }).subscribeOn (Schedulers.io ());
    }
}
