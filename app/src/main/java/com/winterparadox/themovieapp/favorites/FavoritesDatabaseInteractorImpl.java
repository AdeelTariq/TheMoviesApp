package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.FavoriteDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FavoritesDatabaseInteractorImpl implements FavoritesDatabaseInteractor {

    private FavoriteDao favoriteDao;

    public FavoritesDatabaseInteractorImpl (FavoriteDao favoriteDao) {

        this.favoriteDao = favoriteDao;
    }

    @Override
    public Single<List<Movie>> getFavorites () {
        return favoriteDao.getFavorites ().subscribeOn (Schedulers.io ());
    }

    @Override
    public Completable unFavorite (Movie movie) {
        return Completable.fromAction (() ->
                favoriteDao.deleteAll (new Favorite (0, movie)))
                .subscribeOn (Schedulers.io ());
    }
}
