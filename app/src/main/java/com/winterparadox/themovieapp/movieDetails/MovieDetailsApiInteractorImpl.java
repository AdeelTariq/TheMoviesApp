package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Movie;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsApiInteractorImpl implements MovieDetailsApiInteractor {

    private MovieDetailsApiService service;

    public MovieDetailsApiInteractorImpl (MovieDetailsApiService service) {

        this.service = service;
    }

    @Override
    public Single<Movie> movieDetails (long id) {
        return service.movieDetails (id)
                .subscribeOn (Schedulers.io ());
    }
}
