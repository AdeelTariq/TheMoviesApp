package com.winterparadox.themovieapp.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShow {

    @SerializedName("first_air_date")
    String firstAirDate;

    @SerializedName("overview")
    String overview;

    @SerializedName("original_language")
    String originalLanguage;

    @SerializedName("genre_ids")
    List<Object> genreIds;

    @SerializedName("poster_path")
    Object posterPath;

    @SerializedName("origin_country")
    List<String> originCountry;

    @SerializedName("backdrop_path")
    Object backdropPath;

    @SerializedName("media_type")
    String mediaType;

    @SerializedName("popularity")
    int popularity;

    @SerializedName("vote_average")
    int voteAverage;

    @SerializedName("original_name")
    String originalName;

    @SerializedName("name")
    String name;

    @SerializedName("id")
    int id;

    @SerializedName("vote_count")
    int voteCount;

    @Override
    public String toString () {
        return
                "TvShow{" +
                        "first_air_date = '" + firstAirDate + '\'' +
                        ",overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",origin_country = '" + originCountry + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",media_type = '" + mediaType + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",original_name = '" + originalName + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        "}";
    }
}