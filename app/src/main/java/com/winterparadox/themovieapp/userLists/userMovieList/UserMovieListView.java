package com.winterparadox.themovieapp.userLists.userMovieList;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface UserMovieListView extends BaseView {

    void showMovies (List<Movie> movies);

}