package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.base.BasePresenter;

public abstract class HomePresenter extends BasePresenter<HomeView> {

    abstract void fetchData ();
}
