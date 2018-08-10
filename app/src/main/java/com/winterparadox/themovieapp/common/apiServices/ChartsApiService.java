package com.winterparadox.themovieapp.common.apiServices;

import com.winterparadox.themovieapp.common.beans.MoviesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChartsApiService {


    @GET("discover/movie?sort_by=popularity.desc&include_adult=false")
    Single<MoviesResponse> popular (@Query("page") int page);

    @GET("discover/movie?sort_by=release_date.desc&vote_count.gte=10&include_adult=false")
    Single<MoviesResponse> latest (@Query("page") int page);

    @GET("discover/movie?sort_by=vote_average.desc&vote_count.gte=100&include_adult=false")
    Single<MoviesResponse> topRated (@Query("page") int page);

    @GET("discover/movie?sort_by=vote_average.desc&vote_count.gte=100&include_adult=false")
    Single<MoviesResponse> topRatedInGenre (@Query("with_genres") int genreId,
                                            @Query("page") int page);

}
