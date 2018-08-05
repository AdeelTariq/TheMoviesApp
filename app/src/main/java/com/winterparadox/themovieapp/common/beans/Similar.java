package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Similar {

    @SerializedName("page")
    public int page;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("results")
    public List<Movie> movies;

    @SerializedName("total_results")
    public int totalResults;

    @Override
    public String toString () {
        return
                "Similar{" +
                        "page = '" + page + '\'' +
                        ",total_pages = '" + totalPages + '\'' +
                        ",results = '" + movies + '\'' +
                        ",total_results = '" + totalResults + '\'' +
                        "}";
    }
}