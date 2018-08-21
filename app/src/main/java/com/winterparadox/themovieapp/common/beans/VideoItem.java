package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class VideoItem {

    public static final String YOUTUBE_THUMB = "https://img.youtube.com/vi/%s/hqdefault.jpg";
    public static final String YOUTUBE_VIDEO = "https://www.youtube.com/watch?v=%s";

    @SerializedName("site")
    public String site;

    @SerializedName("size")
    public int size;

    @SerializedName("iso_3166_1")
    public String iso31661;

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public String id;

    @SerializedName("type")
    public String type;

    @SerializedName("iso_639_1")
    public String iso6391;

    @SerializedName("key")
    public String key;

    @Override
    public String toString () {
        return
                "VideoItem{" +
                        "site = '" + site + '\'' +
                        ",size = '" + size + '\'' +
                        ",iso_3166_1 = '" + iso31661 + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",type = '" + type + '\'' +
                        ",iso_639_1 = '" + iso6391 + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}