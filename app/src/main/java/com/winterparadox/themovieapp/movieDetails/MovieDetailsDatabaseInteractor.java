package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface MovieDetailsDatabaseInteractor {

    Completable addToCache (Movie movie);

    Completable addToRecentlyViewed (Movie movie);

    Single<Boolean> isFavorite (Movie movie);

    Completable addToFavorite (Movie movie);

    Completable unFavorite (Movie movie);

    Single<Boolean> anyUserListExists ();

    void createDefaultUserLists (List<String> defaultLists);

    Single<List<UserList>> getUserLists ();

    boolean isMovieInList (long movieId, long listId);
}
