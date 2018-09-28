package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

public interface RecentlyViewedDatabaseInteractor {

    Single<List<Movie>> getRecentlyViewed (int count);
}
