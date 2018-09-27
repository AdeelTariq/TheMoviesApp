package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface RecentlyViewedDao {

    @Query("SELECT * FROM Movie INNER JOIN RecentlyViewed ON Movie.id=RecentlyViewed.movieId " +
            "ORDER BY time DESC LIMIT :count ")
    Single<List<Movie>> getRecent (int count);

    @Insert(onConflict = REPLACE)
    Long[] insertAll (RecentlyViewed... movies);

    @Query("SELECT EXISTS(SELECT 1 FROM RecentlyViewed)")
    Flowable<Boolean> anyExists ();
}
