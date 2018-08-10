package com.winterparadox.themovieapp.arch;


public interface Presenter<V> {

    void attachView (V view, Navigator navigator);

    void detachView ();

}
