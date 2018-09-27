package com.winterparadox.themovieapp.common.beans;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Chart implements Serializable {

    public static final int CHART_POPULAR = 0, CHART_LATEST = 1, CHART_TOP_RATED = 2;

    @PrimaryKey
    public long id;

    public String name;

    public String backDropPath;

    public Chart () {
    }

    @Ignore
    public Chart (int id, String name) {

        this.id = id;
        this.name = name;
    }
}
