package com.winterparadox.themovieapp.common.base;


public interface Presenter<V> {

    void attachView (V view);

    void detachView ();

}
