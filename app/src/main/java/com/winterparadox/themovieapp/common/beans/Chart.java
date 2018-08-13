package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

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
