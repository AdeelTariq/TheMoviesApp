package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

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
}
