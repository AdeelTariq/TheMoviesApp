package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class FavoritesPresenter extends BasePresenter<FavoritesView> {

    abstract void unFavorite (Movie movie);

    public abstract void onMovieClicked (Movie movie, Object element);
}
