package com.winterparadox.themovieapp.common.beans;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movieId"),
        indices = {@Index(value = "time")})
public class RecentlyViewed {

    public long time;

    @PrimaryKey
    public long movieId;

    public RecentlyViewed () {
    }

    public RecentlyViewed (long time, Movie movie) {

        this.time = time;
        this.movieId = movie.id;
    }
}
