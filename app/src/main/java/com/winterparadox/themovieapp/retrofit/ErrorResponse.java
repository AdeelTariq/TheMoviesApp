package com.winterparadox.themovieapp.retrofit;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("status_code")
    Integer statusCode;
    @SerializedName("status_message")
    String statusMessage;
    Boolean success;

}
