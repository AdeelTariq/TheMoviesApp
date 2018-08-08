package com.winterparadox.themovieapp.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;

@Database(entities = {Movie.class, RecentlyViewed.class, Favorite.class, Chart.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao ();

    public abstract RecentlyViewedDao recentlyViewedDao ();

    public abstract FavoriteDao favoriteDao ();

    public abstract ChartDao chartDao ();
}
