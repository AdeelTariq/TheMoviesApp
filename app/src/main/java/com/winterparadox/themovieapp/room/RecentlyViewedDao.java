package com.winterparadox.themovieapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface RecentlyViewedDao {

    @Query("SELECT * FROM Movie INNER JOIN RecentlyViewed ON Movie.id=RecentlyViewed.movieId " +
            "ORDER BY time DESC LIMIT :count ")
    Single<List<Movie>> getRecent (int count);

    @Insert(onConflict = REPLACE)
    Long[] insertAll (RecentlyViewed... movies);

}
