package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class ReleaseDatesItem {
    public String note;
    @SerializedName("release_date")
    public String releaseDate;
    public int type;
    @SerializedName("iso_639_1")
    public String iso6391;
    public String certification;

    @Override
    public String toString () {
        return
                "ReleaseDatesItem{" +
                        "note = '" + note + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",type = '" + type + '\'' +
                        ",iso_639_1 = '" + iso6391 + '\'' +
                        ",certification = '" + certification + '\'' +
                        "}";
    }
}
