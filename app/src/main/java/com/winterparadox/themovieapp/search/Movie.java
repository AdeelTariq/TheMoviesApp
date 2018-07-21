package com.winterparadox.themovieapp.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {

    @SerializedName("overview")
    String overview;

    @SerializedName("original_language")
    String originalLanguage;

    @SerializedName("original_title")
    String originalTitle;

    @SerializedName("video")
    boolean video;

    @SerializedName("title")
    String title;

    @SerializedName("genre_ids")
    List<Integer> genreIds;

    @SerializedName("media_type")
    String mediaType;

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("backdrop_path")
    String backdropPath;

    @SerializedName("release_date")
    String releaseDate;

    @SerializedName("vote_average")
    double voteAverage;

    @SerializedName("popularity")
    double popularity;

    @SerializedName("id")
    int id;

    @SerializedName("adult")
    boolean adult;

    @SerializedName("vote_count")
    int voteCount;

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
}