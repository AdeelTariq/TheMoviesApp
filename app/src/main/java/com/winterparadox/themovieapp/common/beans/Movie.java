package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.winterparadox.themovieapp.common.Singleton;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.VisibleForTesting;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(indices = {@Index("title")})
public class Movie implements Serializable {

    public static final int NONE = -99;
    @PrimaryKey
    @SerializedName("id")
    public long id;

    @SerializedName("original_language")
    public String originalLanguage;

    @SerializedName("imdb_id")
    public String imdbId;

    @SerializedName("video")
    public boolean video;

    @SerializedName("title")
    public String title;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("revenue")
    public long revenue;

    @TypeConverters(Movie.class)
    @SerializedName("genres")
    public ArrayList<GenresItem> genres;

    @SerializedName("popularity")
    public double popularity;

    @SerializedName("vote_count")
    public int voteCount;

    @SerializedName("budget")
    public long budget;

    @SerializedName("overview")
    public String overview;

    @SerializedName("original_title")
    public String originalTitle;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("vote_average")
    public double voteAverage;

    @SerializedName("tagline")
    public String tagline;

    @SerializedName("adult")
    public boolean adult;

    @SerializedName("homepage")
    public String homepage;

    @SerializedName("status")
    public String status;

    @TypeConverters(Movie.class)
    @SerializedName("release_dates")
    public ReleaseDates releaseDates;

    @TypeConverters(Movie.class)
    @SerializedName("credits")
    public Credits credits;

    @TypeConverters(Movie.class)
    @SerializedName("similar")
    public Similar similar;

    @TypeConverters(Movie.class)
    @SerializedName("videos")
    public Videos videos;

    @Ignore
    public String year;

    public Movie () {
    }

    @VisibleForTesting
    @Ignore
    public Movie (long id) {
        this.id = id;
    }

    @VisibleForTesting
    @Ignore
    public Movie (long id, String title) {
        this.id = id;
        this.title = title;
    }

    @VisibleForTesting
    @Ignore
    public Movie (String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int hashCode () {
        return (int) id;
    }

    @Override
    public boolean equals (Object obj) {
        if ( obj instanceof Movie ) {
            return ((Movie) obj).id == id &&
                    (year == null || year.equals (((Movie) obj).year));
        }
        return super.equals (obj);
    }

    @Override
    public String toString () {
        return
                "Movie1{" +
                        "original_language = '" + originalLanguage + '\'' +
                        ",imdb_id = '" + imdbId + '\'' +
                        ",video = '" + video + '\'' +
                        ",title = '" + title + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",revenue = '" + revenue + '\'' +
                        ",genres = '" + genres + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",id = '" + id + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        ",budget = '" + budget + '\'' +
                        ",overview = '" + overview + '\'' +
                        ",original_title = '" + originalTitle + '\'' +
                        ",runtime = '" + runtime + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",tagline = '" + tagline + '\'' +
                        ",adult = '" + adult + '\'' +
                        ",homepage = '" + homepage + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }

    @TypeConverter
    public static ArrayList<GenresItem> storedStringToList (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<ArrayList<GenresItem>> () {
        }.getType ());
    }

    @TypeConverter
    public static String listToStoredString (ArrayList<GenresItem> arrayList) {
        return Singleton.gson.toJson (arrayList);
    }

    @TypeConverter
    public static ReleaseDates storedStringToReleaseDates (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<ReleaseDates> () {
        }.getType ());
    }

    @TypeConverter
    public static String releaseDatesToStoredString (ReleaseDates dates) {
        return Singleton.gson.toJson (dates);
    }

    @TypeConverter
    public static Credits storedStringToCredits (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<Credits> () {
        }.getType ());
    }

    @TypeConverter
    public static String creditsToStoredString (Credits credits) {
        return Singleton.gson.toJson (credits);
    }

    @TypeConverter
    public static Similar storedStringToSimilar (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<Similar> () {
        }.getType ());
    }

    @TypeConverter
    public static String similarToStoredString (Similar similar) {
        return Singleton.gson.toJson (similar);
    }

    @TypeConverter
    public static Videos storedStringToVideos (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<Videos> () {
        }.getType ());
    }

    @TypeConverter
    public static String VideosToStoredString (Videos videos) {
        return Singleton.gson.toJson (videos);
    }

}