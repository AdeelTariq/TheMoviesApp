package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface SearchResultView extends BaseView {

    void showMovies (List<Movie> movies);

    void addMovies (List<Movie> movies);

    void showPageProgress ();

    void hidePageProgress ();

    void restoreMovies (int visiblePos, List items, int page);
}
