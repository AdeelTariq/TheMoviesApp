package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface HostDatabaseInteractor {

    Long insertChart (Chart chart);

    Long updateChart (Chart chart);

    Single<Chart> getChart (long id);

    Flowable<Boolean> anyFavoriteExists ();

    Flowable<Boolean> anyRecentyViewedExists ();

    Single<List<Movie>> getSuggestions (String query);
}
