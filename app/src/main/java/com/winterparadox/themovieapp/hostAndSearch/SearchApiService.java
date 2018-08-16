package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.SearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApiService {


    @GET("search/movie")
    Single<SearchResponse> search (@Query("query") String query, @Query("page") int page);

}
