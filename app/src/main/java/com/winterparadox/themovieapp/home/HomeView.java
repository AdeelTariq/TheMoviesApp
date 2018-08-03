package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.base.BaseView;

import java.util.ArrayList;

public interface HomeView extends BaseView {

    void showMovies (ArrayList<Object> movies);

    String popularTitle ();

    String latestTitle ();

    void clearView ();
}
