package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class HomeApiInteractorImpl implements HomeApiInteractor {

    private HomeApiService service;

    public HomeApiInteractorImpl (HomeApiService service) {

        this.service = service;
    }

    @Override
    public Single<List<Movie>> popularMovies () {
        return service.popularMovies ()
                .subscribeOn (Schedulers.io ())
                .flatMap (response -> Single.just (response.results));
    }

    @Override
    public Single<List<Movie>> upcomingMovies () {
        return service.upcomingMovies ()
                .subscribeOn (Schedulers.io ())
                .flatMap (response -> Single.just (response.results));
    }
}
