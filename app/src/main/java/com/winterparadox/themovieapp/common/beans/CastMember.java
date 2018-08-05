package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CastMember {

    @SerializedName("cast_id")
    public int castId;

    @SerializedName("character")
    public String character;

    @SerializedName("gender")
    public int gender;

    @SerializedName("credit_id")
    public String creditId;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_path")
    public String profilePath;

    @SerializedName("id")
    public int id;

    @SerializedName("order")
    public int order;

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