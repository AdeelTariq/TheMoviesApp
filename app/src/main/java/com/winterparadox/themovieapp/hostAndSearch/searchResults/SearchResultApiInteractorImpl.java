package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.hostAndSearch.SearchApiService;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SearchResultApiInteractorImpl implements SearchResultApiInteractor {

    private ChartsApiService service;
    private SearchApiService searchService;

    public SearchResultApiInteractorImpl (ChartsApiService service,
                                          SearchApiService searchService) {
        this.service = service;
        this.searchService = searchService;
    }

    @Override
    public Single<List<Movie>> popularMovies (int page) {
        return service.popular (page + 1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> latestMovies (int page) {
        return service.latest (page + 1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> topRatedMovies (int page) {
        return service.topRated (page + 1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> topRatedInGenre (long genre, int page) {
        return service.topRatedInGenre (genre, page + 1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> search (String query, int page) {
        return searchService.search (query, page + 1)
                .subscribeOn (Schedulers.io ())
                .map (searchResponse -> searchResponse.results);
    }
}
