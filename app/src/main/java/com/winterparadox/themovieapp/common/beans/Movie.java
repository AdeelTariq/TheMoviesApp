package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {

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

    @SerializedName("genre_ids")
    public List<Integer> genreIds;

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

    @SerializedName("id")
    public int id;

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
}