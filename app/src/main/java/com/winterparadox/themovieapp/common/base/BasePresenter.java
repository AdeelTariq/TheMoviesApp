package com.winterparadox.themovieapp.common.base;

/**
 * Created by Adeel on 8/21/2017.
 */

public abstract class BasePresenter<V extends BaseView> implements Presenter<V> {

    public V view;

    @Override
    public void attachView (V view) {

        this.view = view;
    }

    @Override
    public void detachView () {
        view = null;
    }
}
