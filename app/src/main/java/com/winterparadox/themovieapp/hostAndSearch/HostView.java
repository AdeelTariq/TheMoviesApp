package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface HostView extends BaseView {

    void showFavoritesMenu (boolean show);

    void showRecentMenu (boolean show);

    /**
     * Names must be sorted in the order they are to be displayed
     * And they should correspond to default userList constants in following order:
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_POPULAR},
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_LATEST},
     * {@link com.winterparadox.themovieapp.common.beans.Chart#CHART_TOP_RATED}
     *
     * @return names of default charts to be added to database
     */
    List<String> getDefaultCharts ();

    void showSuggestions (List<Movie> movies);

    void clearSuggestions ();
}
