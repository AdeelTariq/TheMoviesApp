package com.winterparadox.themovieapp.arch;

/**
 * Created by Adeel on 8/21/2017.
 */

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {

    protected V view;
    protected Navigator navigator;

    @Override
    public void attachView (V view, Navigator navigator) {

        this.view = view;
        this.navigator = navigator;
    }

    @Override
    public void detachView () {
        view = null;
        navigator = null;
    }
}
