package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface ChartMovieListApiInteractor {

    Single<List<Movie>> popularMovies ();

    Single<List<Movie>> latestMovies ();

    Single<List<Movie>> topRatedMovies ();
}
