package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;
import com.winterparadox.themovieapp.common.room.UserListDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsDatabaseInteractorImpl implements MovieDetailsDatabaseInteractor {

    private MovieDao movieDao;
    private RecentlyViewedDao recentlyViewedDao;
    private FavoriteDao favoriteDao;
    private UserListDao userListDao;

    public MovieDetailsDatabaseInteractorImpl (MovieDao movieDao, RecentlyViewedDao
            recentlyViewedDao, FavoriteDao favoriteDao, UserListDao userListDao) {
        this.movieDao = movieDao;
        this.recentlyViewedDao = recentlyViewedDao;
        this.favoriteDao = favoriteDao;
        this.userListDao = userListDao;
    }

    @Override
    public Completable addToCache (Movie movie) {
        return Completable.fromAction (() -> movieDao.insertAll (movie))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable addToRecentlyViewed (Movie movie) {
        return Completable.fromAction (() -> recentlyViewedDao
                .insertAll (new RecentlyViewed (System.currentTimeMillis (), movie)))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Single<Boolean> isFavorite (Movie movie) {
        return favoriteDao.isFavorite (movie.id).subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable addToFavorite (Movie movie) {
        return Completable.fromAction (() -> favoriteDao
                .insertAll (new Favorite (System.currentTimeMillis (), movie)))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable unFavorite (Movie movie) {
        return Completable.fromAction (() -> favoriteDao.deleteAll (new Favorite (0, movie)))
                .subscribeOn (Schedulers.io ());
    }

    @Override
    public Single<Boolean> anyUserListExists () {
        return userListDao.anyListExists ().subscribeOn (Schedulers.io ());
    }

    @Override
    public void createDefaultUserLists (List<String> defaultLists) {
        for ( String defaultList : defaultLists ) {
            userListDao.insertList (new UserList (defaultList));
        }
    }

    @Override
    public Single<List<UserList>> getUserLists () {
        return userListDao.getUserListsOnce ().subscribeOn (Schedulers.io ());
    }

    @Override
    public boolean isMovieInList (long movieId, long listId) {
        return userListDao.isInList (movieId, listId);
    }
}
