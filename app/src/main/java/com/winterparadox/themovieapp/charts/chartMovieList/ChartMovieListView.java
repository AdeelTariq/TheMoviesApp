package com.winterparadox.themovieapp.charts.chartMovieList;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface ChartMovieListView extends BaseView {

    void showMovies (List<Movie> movies);
}
