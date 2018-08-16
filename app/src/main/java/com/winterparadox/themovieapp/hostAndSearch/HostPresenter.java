package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class HostPresenter extends BasePresenter<HostView> {

    public abstract void fetchChartData ();

    public abstract void getSuggestions (String query);

    public abstract void onRecentlyViewedClicked ();

    public abstract void onFavoritesClicked ();

    public abstract void onMyListsClicked ();

    public abstract void onChartsClicked ();

    public abstract void onMovieSuggestionClicked (Movie movie);

    public abstract void search (String query);
}
