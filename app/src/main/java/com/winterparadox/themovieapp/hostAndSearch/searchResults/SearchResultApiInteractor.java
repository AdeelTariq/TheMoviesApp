package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface SearchResultApiInteractor {

    Single<List<Movie>> popularMovies (int page);

    Single<List<Movie>> latestMovies (int page);

    Single<List<Movie>> topRatedMovies (int page);

    Single<List<Movie>> topRatedInGenre (long genre, int page);

    Single<List<Movie>> search (String query, int page);
}
