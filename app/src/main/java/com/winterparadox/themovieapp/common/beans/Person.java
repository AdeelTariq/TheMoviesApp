package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person {

    @SerializedName("media_type")
    String mediaType;

    @SerializedName("known_for")
    List<Object> knownFor;    // can be either tv or movie

    @SerializedName("popularity")
    double popularity;

    @SerializedName("name")
    String name;

    @SerializedName("profile_path")
    String profilePath;

    @SerializedName("id")
    int id;

    @SerializedName("adult")
    boolean adult;

    @Override
    public String toString () {
        return
                "Person{" +
                        "media_type = '" + mediaType + '\'' +
                        ",known_for = '" + knownFor + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        "}";
    }
}