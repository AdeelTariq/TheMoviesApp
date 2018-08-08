package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.common.beans.GenresResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ConfigurationApiService {


    @GET("genre/movie/list")
    Single<GenresResponse> genres ();

}
