package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse extends BaseResponse {

    @SerializedName("results")
    public List<Movie> results;
}