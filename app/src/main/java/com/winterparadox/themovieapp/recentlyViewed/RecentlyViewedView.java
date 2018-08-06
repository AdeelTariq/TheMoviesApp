package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface RecentlyViewedView extends BaseView {

    void showMovies (List<Movie> movies);

}
