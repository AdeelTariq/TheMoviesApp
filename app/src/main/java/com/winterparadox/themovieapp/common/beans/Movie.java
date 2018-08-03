package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(indices = {@Index("title")})
public class Movie {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("overview")
    public String overview;

    @SerializedName("original_language")
    public String originalLanguage;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("video")
    public boolean video;

    @SerializedName("title")
    public String title;

    @TypeConverters(Movie.class)
    @SerializedName("genre_ids")
    public ArrayList<Integer> genreIds;

    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("vote_average")
    public double voteAverage;

    @SerializedName("popularity")
    public double popularity;


    @SerializedName("adult")
    public boolean adult;

    @SerializedName("vote_count")
    public int voteCount;

    @Override
    public String toString () {
        return
                "Movie{" +
                        "overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",original_title = '" + originalTitle + '\'' +
                        ",video = '" + video + '\'' +
                        ",title = '" + title + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",media_type = '" + mediaType + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        "}";
    }

    @TypeConverter
    public static ArrayList<Integer> storedStringToIntegers (String value) {
        List<String> strings = Arrays.asList (value.split ("\\s*,\\s*"));
        ArrayList<Integer> ints = new ArrayList<> ();
        for ( String string : strings ) {
            ints.add (Integer.valueOf (string));
        }
        return ints;
    }

    @TypeConverter
    public static String languagesToStoredString (ArrayList<Integer> il) {
        String value = "";

        for ( Integer i : il )
            value += i + ",";

        return value;
    }
}