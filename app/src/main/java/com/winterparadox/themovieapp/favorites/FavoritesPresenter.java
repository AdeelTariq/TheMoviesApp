package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.common.base.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class FavoritesPresenter extends BasePresenter<FavoritesView> {

    abstract void unFavorite (Movie movie);
}
