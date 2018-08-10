package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("file_path")
    public String filePath;
    public String id;
    @SerializedName("aspect_ratio")
    public float aspectRatio;
}
