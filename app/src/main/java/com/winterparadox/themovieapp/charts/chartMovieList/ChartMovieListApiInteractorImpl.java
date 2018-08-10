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
    public Single<List<Movie>> topRatedInGenre (int genre, int page) {
        return service.topRatedInGenre (genre, page + 1)
                .subscribeOn (Schedulers.io ())
                .map (moviesResponse -> moviesResponse.results);
    }
}
