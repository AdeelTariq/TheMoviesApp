package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface HomeDatabaseInteractor {

    Single<List<Movie>> getHomeFavorites ();

    Single<List<Movie>> getHomeRecents ();
}
