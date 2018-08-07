package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {

    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("known_for")
    public List<Object> knownFor;    // can be either tv or movie

    @SerializedName("popularity")
    public double popularity;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_path")
    public String profilePath;

    @SerializedName("id")
    public int id;

    @SerializedName("adult")
    public boolean adult;

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