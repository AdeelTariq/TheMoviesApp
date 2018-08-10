package com.winterparadox.themovieapp.arch;

/**
 * Created by Adeel on 9/8/2017.
 */

public interface BaseView {

    void showProgress ();

    void hideProgress ();

    void showMessage (String message);

    void showError (String message);

}
