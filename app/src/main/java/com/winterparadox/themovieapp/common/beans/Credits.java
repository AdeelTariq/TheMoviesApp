package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("cast")
    public List<CastMember> cast;

    @SerializedName("crew")
    public List<CrewMember> crew;

    @Override
    public String toString () {
        return
                "Credits{" +
                        "cast = '" + cast + '\'' +
                        ",crew = '" + crew + '\'' +
                        "}";
    }
}