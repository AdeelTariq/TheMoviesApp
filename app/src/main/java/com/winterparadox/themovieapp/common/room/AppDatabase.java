package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, RecentlyViewed.class, Favorite.class, Chart.class,
        UserList.class, UserListItem.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao ();

    public abstract RecentlyViewedDao recentlyViewedDao ();

    public abstract FavoriteDao favoriteDao ();

    public abstract ChartDao chartDao ();

    public abstract UserListDao userListDao ();
}
