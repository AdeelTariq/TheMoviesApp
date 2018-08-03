package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface HomeApiInteractor {

    Single<List<Movie>> popularMovies ();

    Single<List<Movie>> latestMovies ();
}
