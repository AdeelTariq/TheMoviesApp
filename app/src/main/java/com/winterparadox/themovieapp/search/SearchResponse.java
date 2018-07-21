package com.winterparadox.themovieapp.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Object> results;
}