package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguagesItem {

    @SerializedName("name")
    public String name;

    @SerializedName("iso_639_1")
    public String iso6391;

    @Override
    public String toString () {
        return
                "SpokenLanguagesItem{" +
                        "name = '" + name + '\'' +
                        ",iso_639_1 = '" + iso6391 + '\'' +
                        "}";
    }
}