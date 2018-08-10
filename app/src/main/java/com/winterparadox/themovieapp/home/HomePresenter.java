package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class HomePresenter extends BasePresenter<HomeView> {

    abstract void fetchData ();

    public abstract void onMovieClicked (Movie movie, Object element);

    public abstract void onChartClicked (Chart chart);

    public abstract void onFavoritesClicked ();

    public abstract void onRecentlyViewedClicked ();
}
