package com.winterparadox.themovieapp.common.beans;

import java.util.ArrayList;

public class ReleaseDates {
    public ArrayList<RegionItem> results;

    @Override
    public String toString () {
        return
                "ReleaseDates{" +
                        "results = '" + results + '\'' +
                        "}";
    }
}