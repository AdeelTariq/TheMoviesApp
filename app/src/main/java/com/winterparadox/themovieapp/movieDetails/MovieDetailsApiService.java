package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Movie;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The movie db api for home screen
 */
public interface MovieDetailsApiService {

    @GET("movie/{id}?&append_to_response=release_dates,credits,similar,videos")
    Single<Movie> movieDetails (@Path("id") long id);

}
