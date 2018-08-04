package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CastMember {

    @SerializedName("cast_id")
    private int castId;

    @SerializedName("character")
    private String character;

    @SerializedName("gender")
    private int gender;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("id")
    private int id;

    @SerializedName("order")
    private int order;

    @Override
    public String toString () {
        return
                "CastMember{" +
                        "cast_id = '" + castId + '\'' +
                        ",character = '" + character + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",credit_id = '" + creditId + '\'' +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",order = '" + order + '\'' +
                        "}";
    }
}