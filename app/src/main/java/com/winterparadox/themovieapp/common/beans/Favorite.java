package com.winterparadox.themovieapp.common.beans;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    @Override
    public boolean equals (@Nullable Object obj) {
        if ( obj instanceof Favorite ) {
            return movieId == ((Favorite) obj).movieId;
        }
        return super.equals (obj);
    }
}
