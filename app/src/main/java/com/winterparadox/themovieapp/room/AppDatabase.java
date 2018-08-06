package com.winterparadox.themovieapp.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;

@Database(entities = {Movie.class, RecentlyViewed.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao ();

    public abstract RecentlyViewedDao recentlyViewedDao ();
}
