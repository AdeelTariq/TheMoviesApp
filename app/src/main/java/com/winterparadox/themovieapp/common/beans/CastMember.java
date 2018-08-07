package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CastMember extends Person {

    @SerializedName("cast_id")
    public int castId;

    @SerializedName("character")
    public String character;

    @SerializedName("order")
    public int order;

    @Override
    public String toString () {
        return
                "CastMember{" +
                        "cast_id = '" + castId + '\'' +
                        ",character = '" + character + '\'' +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",order = '" + order + '\'' +
                        "}";
    }
}