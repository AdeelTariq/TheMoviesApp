package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.arch.BasePresenter;

public abstract class HostPresenter extends BasePresenter<HostView> {

    public abstract void fetchChartData ();

    public abstract void onRecentlyViewedClicked ();

    public abstract void onFavoritesClicked ();

    public abstract void onMyListsClicked ();

    public abstract void onChartsClicked ();
}
