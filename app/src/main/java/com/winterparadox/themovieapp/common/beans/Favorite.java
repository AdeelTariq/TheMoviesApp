package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "id",
        childColumns = "movieId"),
        indices = {@Index(value = "time")})
public class Favorite {

    public long time;

    @PrimaryKey
    public long movieId;

    public Favorite () {
    }

    public Favorite (long time, Movie movie) {

        this.time = time;
        this.movieId = movie.id;
    }
}
