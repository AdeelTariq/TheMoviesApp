package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface ChartMovieListApiInteractor {

    /**
     * Get list of most popular movies / list of movies sorted by tmdb popularity metric
     *
     * @param page page of the list. Starts with 1
     * @return list of movies
     */
    Single<List<Movie>> popularMovies (int page);

    /**
     * Get list of lates movies / list of movies sorted by release dates
     * @param page page of the list. Starts with 1
     * @return list of movies
     */
    Single<List<Movie>> latestMovies (int page);

    /**
     * Get list of top rated movies / list of movies sorted by tmdb rating metric
     * @param page page of the list. Starts with 1
     * @return list of movies
     */
    Single<List<Movie>> topRatedMovies (int page);

    /**
     * Get list of top rated movies in a genre
     * @param genre genre id that was obtained by calling configuration api method
     * @param page page of the list. Starts with 1
     * @return list of movies
     */
    Single<List<Movie>> topRatedInGenre (long genre, int page);
}
