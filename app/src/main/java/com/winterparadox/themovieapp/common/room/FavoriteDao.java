package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface FavoriteDao {

    @Query("SELECT * FROM Movie INNER JOIN Favorite ON Movie.id=Favorite.movieId " +
            "ORDER BY time DESC ")
    Single<List<Movie>> getFavorites ();

    @Query("SELECT * FROM Movie INNER JOIN Favorite ON Movie.id=Favorite.movieId " +
            "ORDER BY movie.voteAverage DESC LIMIT :count")
    Single<List<Movie>> getHomeFavorites (int count);


    @Insert(onConflict = REPLACE)
    Long[] insertAll (Favorite... movies);

    @Delete
    void deleteAll (Favorite... movies);

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite WHERE movieId=:movieId)")
    Single<Boolean> isFavorite (long movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite)")
    Flowable<Boolean> anyExists ();

}
