package com.winterparadox.themovieapp.common.beans;

public class HomeSection {


    public static final int SECTION_RECENT = 0,
            SECTION_FAVORITES = 1,
            SECTION_UPCOMING = 2,
            SECTION_POPULAR = 3;

    public int id;
    public String name;

    public HomeSection (int id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals (Object obj) {
        if ( obj instanceof HomeSection )
            return ((HomeSection) obj).id == id;
        return super.equals (obj);
    }
}
