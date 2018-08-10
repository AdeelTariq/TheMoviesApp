package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Movie;

public abstract class RecentlyViewedPresenter extends BasePresenter<RecentlyViewedView> {

    public abstract void onMovieClicked (Movie movie, Object element);
}
