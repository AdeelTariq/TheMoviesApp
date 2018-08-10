package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

public abstract class MovieDetailsPresenter extends BasePresenter<MovieDetailsView> {

    protected Movie movie;

    void attachView (MovieDetailsView view, Movie movie, Navigator navigator) {
        this.movie = movie;
        super.attachView (view, navigator);
    }

    abstract boolean isMovieFav ();

    abstract void setMovieFav (boolean isFav);

    public abstract void onMovieClicked (Movie movie, Object element);

    public abstract void onPersonClicked (Person member, Object view);
}
