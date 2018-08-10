package com.winterparadox.themovieapp.arch;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

public interface Navigator {

    /**
     * @param movie movie to be opened
     * @param view  View object being passed as Object
     */
    void openMovie (Movie movie, Object view);

    void openFavorites ();

    void openRecentlyViewed ();

    void openMyLists ();

    void openCharts ();

    void openPerson (Person person, Object view);

    void openChartMovieList (Chart chart);
}
