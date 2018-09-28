package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface FavoritesDatabaseInteractor {

    Single<List<Movie>> getFavorites ();

    Completable unFavorite (Movie movie);
}
