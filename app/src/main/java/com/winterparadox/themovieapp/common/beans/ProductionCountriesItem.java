package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class ProductionCountriesItem {

    @SerializedName("iso_3166_1")
    public String iso31661;

    @SerializedName("name")
    public String name;

    @Override
    public String toString () {
        return
                "ProductionCountriesItem{" +
                        "iso_3166_1 = '" + iso31661 + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}