package com.winterparadox.themovieapp.common;

import java.util.Locale;

public class PresenterUtils {

    public static String runtimeString (int runtime) {
        return String.format (Locale.getDefault (),
                "%dh %dm", runtime / 60, runtime % 60);
    }

}
