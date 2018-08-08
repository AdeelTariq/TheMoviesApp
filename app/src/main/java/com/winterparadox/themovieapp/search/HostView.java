package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

import java.util.List;

public interface HostView extends BaseView {
    /**
     * @param movie movie to be opened
     * @param view  View object being passed as Object
     */
    void openMovie (Movie movie, Object view);

    void openFavorites ();

    void openRecentlyViewed ();

    void showFavoritesMenu (boolean show);

    void showRecentMenu (boolean show);

    void openPerson (Person person, Object view);

    /**
     * Names must be sorted in the order they are to be displayed
     * And they should correspond to default chart constants in following order:
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_POPULAR},
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_LATEST},
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_TOP_RATED}
     *
     * @return names of default charts to be added to database
     */
    List<String> getDefaultCharts ();
}
