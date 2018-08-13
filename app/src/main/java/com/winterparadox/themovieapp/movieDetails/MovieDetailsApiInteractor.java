package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Movie;

import io.reactivex.Single;

public interface MovieDetailsApiInteractor {
    Single<Movie> movieDetails (long id);
}
