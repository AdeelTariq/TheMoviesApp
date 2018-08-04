package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegionItem {
    @SerializedName("release_dates")
    public ArrayList<ReleaseDatesItem> releaseDates;
    @SerializedName("iso_3166_1")
    public String iso31661;

    @Override
    public String toString () {
        return
                "RegionItem{" +
                        "release_dates = '" + releaseDates + '\'' +
                        ",iso_3166_1 = '" + iso31661 + '\'' +
                        "}";
    }
}