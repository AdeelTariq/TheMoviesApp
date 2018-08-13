package com.winterparadox.themovieapp.common.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    Single<List<Movie>> getAll ();

    @Query("SELECT * FROM Movie WHERE id IN (:movieIds)")
    Single<List<Movie>> loadAllByIds (int[] movieIds);

    @Query("SELECT * FROM Movie WHERE title LIKE :name LIMIT 1")
    Movie findByName (String name);

    @Insert(onConflict = REPLACE)
    Long[] insertAll (Movie... movies);

}
