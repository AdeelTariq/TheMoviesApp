package com.winterparadox.themovieapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    List<Movie> getAll ();

    @Query("SELECT * FROM Movie WHERE id IN (:movieIds)")
    List<Movie> loadAllByIds (int[] movieIds);

    @Query("SELECT * FROM Movie WHERE title LIKE :name LIMIT 1")
    Movie findByName (String name);

    @Insert
    void insertAll (Movie... movies);

    @Delete
    void delete (Movie movie);
}
