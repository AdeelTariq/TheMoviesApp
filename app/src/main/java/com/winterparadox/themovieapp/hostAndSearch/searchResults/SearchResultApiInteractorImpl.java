package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.hostAndSearch.SearchApiService;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SearchResultApiInteractorImpl implements SearchResultApiInteractor {

    private SearchApiService searchService;

    public SearchResultApiInteractorImpl (SearchApiService searchService) {
        this.searchService = searchService;
    }

    @Override
    public Single<List<Movie>> search (String query, int page) {
        return searchService.search (query, page + 1)
                .subscribeOn (Schedulers.io ())
                .map (searchResponse -> searchResponse.results);
    }
}
