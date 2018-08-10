package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.arch.BaseView;

import java.util.ArrayList;

public interface HomeView extends BaseView {

    void showMovies (ArrayList<Object> movies);

    String popularTitle ();

    String upcomingTitle ();

    void clearView ();

    String recentlyTitle ();

    String favoriteTitle ();
}
