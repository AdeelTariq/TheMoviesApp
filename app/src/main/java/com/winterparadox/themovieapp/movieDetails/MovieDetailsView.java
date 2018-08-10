package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

public interface MovieDetailsView extends BaseView {

    void showMovie (Movie movie, String year);

    void showAdditionalDetails (Movie movie, String ageRating, String runtime, String genres);

    void showFavorite (boolean isFavorite);
}
