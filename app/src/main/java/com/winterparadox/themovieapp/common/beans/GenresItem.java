package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.VisibleForTesting;
import androidx.room.Ignore;

public class GenresItem {

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public int id;

    public GenresItem () {
    }

    @VisibleForTesting
    @Ignore
    public GenresItem (String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return
                "GenresItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}