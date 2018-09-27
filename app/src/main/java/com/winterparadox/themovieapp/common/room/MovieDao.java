package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    Single<List<Movie>> getAll ();

    @Query("SELECT * FROM Movie WHERE id IN (:movieIds)")
    Single<List<Movie>> loadAllByIds (int[] movieIds);

    @Query("SELECT * FROM Movie WHERE title LIKE :query LIMIT :count")
    Single<List<Movie>> search (String query, int count);

    @Insert(onConflict = REPLACE)
    Long[] insertAll (Movie... movies);


}
