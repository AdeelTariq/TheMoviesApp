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

    @Override
    public boolean equals (@Nullable Object obj) {
        if ( obj instanceof RecentlyViewed ) {
            return movieId == ((RecentlyViewed) obj).movieId;
        }
        return super.equals (obj);
    }
}
