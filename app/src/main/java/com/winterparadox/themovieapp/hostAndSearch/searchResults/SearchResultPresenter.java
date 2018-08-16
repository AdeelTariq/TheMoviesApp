package com.winterparadox.themovieapp.hostAndSearch.searchResults;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public abstract class SearchResultPresenter extends BasePresenter<SearchResultView> {

    protected String query;

    public void attachView (SearchResultView view, String query, Navigator navigator) {
        this.query = query;
        super.attachView (view, navigator);
    }

    public abstract void fetchDataPage (int page);

    public abstract void onMovieClicked (Movie movie, Object element);

    public abstract void saveState (int visibleItemPosition, List<Object> items);

    public abstract void onRetryClicked ();
}
