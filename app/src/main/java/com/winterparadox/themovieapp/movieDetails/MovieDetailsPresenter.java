package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.base.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class MovieDetailsPresenter extends BasePresenter<MovieDetailsView> {

    protected Movie movie;

    void attachView (MovieDetailsView view, Movie movie) {
        this.movie = movie;
        super.attachView (view);
    }

    abstract boolean isMovieFav ();

    abstract void setMovieFav (boolean isFav);
}
