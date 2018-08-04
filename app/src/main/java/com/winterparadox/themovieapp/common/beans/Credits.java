package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Credits {

    @SerializedName("cast")
    private ArrayList<CastMember> cast;

    @SerializedName("crew")
    private ArrayList<CrewMember> crew;

    @Override
    public String toString () {
        return
                "Credits{" +
                        "cast = '" + cast + '\'' +
                        ",crew = '" + crew + '\'' +
                        "}";
    }
}