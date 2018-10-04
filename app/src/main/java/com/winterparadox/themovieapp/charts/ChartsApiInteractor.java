package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.beans.Chart;

import java.util.List;

import io.reactivex.Single;

public interface ChartsApiInteractor {

    /**
     * Get all the genres from tmdb api
     *
     * @return genre list
     */
    Single<List<Chart>> genres ();

    /**
     * Get the backdrop image of most popular movie
     * @param chart chart the backdrop image will be set to
     * @return chart with the image
     */
    Single<Chart> popularMovieBackdrop (Chart chart);

    /**
     * Get the backdrop image of latest movie
     * @param chart chart the backdrop image will be set to
     * @return chart with the image
     */
    Single<Chart> latestMovieBackdrop (Chart chart);

    /**
     * Get the backdrop image of top rated movie
     * @param chart chart the backdrop image will be set to
     * @return chart with the image
     */
    Single<Chart> topRatedMovieBackdrop (Chart chart);

    /**
     * Get the backdrop image of top movie of a genre
     * @param chart chart the backdrop image will be set to
     * @return chart with the image
     */
    Single<Chart> genreMovieBackdrop (Chart chart);
}
