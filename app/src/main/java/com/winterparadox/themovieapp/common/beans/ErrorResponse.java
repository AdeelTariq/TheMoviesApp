package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("status_code")
    public int statusCode;
    @SerializedName("status_message")
    public String statusMessage;
    Boolean success;

}
