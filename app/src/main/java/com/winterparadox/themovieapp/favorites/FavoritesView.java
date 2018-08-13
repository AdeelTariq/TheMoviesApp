package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface FavoritesView extends BaseView {

    void showMovies (List<Movie> movies, int lastVisibleItem);

}
