package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ChartMovieListApiInteractorImpl implements ChartMovieListApiInteractor {

    private ChartsApiService service;

    public ChartMovieListApiInteractorImpl (ChartsApiService service) {

        this.service = service;
    }

    @Override
    public Single<List<Movie>> popularMovies () {
        return service.popular (1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> latestMovies () {
        return service.latest (1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }

    @Override
    public Single<List<Movie>> topRatedMovies () {
        return service.topRated (1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }
}
