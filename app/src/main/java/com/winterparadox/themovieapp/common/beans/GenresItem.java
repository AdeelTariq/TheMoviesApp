package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class GenresItem {

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public int id;

    @Override
    public String toString () {
        return
                "GenresItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}