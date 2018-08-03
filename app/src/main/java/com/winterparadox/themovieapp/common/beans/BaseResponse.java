package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;
}