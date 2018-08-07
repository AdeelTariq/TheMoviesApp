package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.MoviesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * The movie db api for home screen
 */
public interface HomeApiService {

    @GET("movie/popular")
    Single<MoviesResponse> popularMovies ();

    @GET("movie/upcoming")
    Single<MoviesResponse> upcomingMovies ();

}
