package com.winterparadox.themovieapp.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PresenterUtils {

    public static String runtimeString (int runtime) {
        return String.format (Locale.getDefault (),
                "%dh %dm", runtime / 60, runtime % 60);
    }

    public static <T> List<T> asSortedList (Collection<T> c, Comparator<T> comparator) {
        List<T> list = new ArrayList<T> (c);
        java.util.Collections.sort (list, comparator);
        return list;
    }

}
