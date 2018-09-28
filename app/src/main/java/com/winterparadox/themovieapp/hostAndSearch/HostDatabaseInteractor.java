package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface HostDatabaseInteractor {

    Long insert (Chart chart);

    Long update (Chart chart);

    Single<Chart> getChart (long id);

    Flowable<Boolean> anyFavoriteExists ();

    Flowable<Boolean> anyRecentyViewedExists ();

    Single<List<Movie>> getSuggestios (String query);
}
