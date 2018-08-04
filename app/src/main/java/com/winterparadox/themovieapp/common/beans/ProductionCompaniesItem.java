package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class ProductionCompaniesItem {

    @SerializedName("logo_path")
    public String logoPath;

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public int id;

    @SerializedName("origin_country")
    public String originCountry;

    @Override
    public String toString () {
        return
                "ProductionCompaniesItem{" +
                        "logo_path = '" + logoPath + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",origin_country = '" + originCountry + '\'' +
                        "}";
    }
}