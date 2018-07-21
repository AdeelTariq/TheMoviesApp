package com.winterparadox.themovieapp.search;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {


    @GET("/search/multi")
    Single<SearchResponse> search (@Query("query") String query, @Query("page") int page);

}
