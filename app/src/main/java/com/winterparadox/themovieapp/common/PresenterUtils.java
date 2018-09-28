package com.winterparadox.themovieapp.common;

import com.winterparadox.themovieapp.charts.ChartsDatabaseInteractor;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.hostAndSearch.HostDatabaseInteractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

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


    @NonNull
    public static Disposable createCharts (Single<List<Chart>> generes,
                                           Function<Chart, SingleSource<? extends Chart>>
                                                    backDropFunction,
                                           Object database,
                                           List<String> defaultCharts) {
        return generes
                .map (charts -> {       // add gather charts and genres
                    for ( int i = defaultCharts.size () - 1; i >= 0; i-- ) {
                        String defaultChart = defaultCharts.get (i);
                        charts.add (0, new Chart (i, defaultChart));
                    }

                    return charts;

                }).toObservable ()
                .flatMapIterable (charts -> charts)
                .flatMapSingle (chart -> {          // save to database if not already saved
                    if ( database instanceof ChartsDatabaseInteractor ) {
                        ((ChartsDatabaseInteractor) database).insert (chart);
                        return ((ChartsDatabaseInteractor) database).getChart (chart.id);
                    }
                    if ( database instanceof HostDatabaseInteractor ) {
                        ((HostDatabaseInteractor) database).insert (chart);
                        return ((HostDatabaseInteractor) database).getChart (chart.id);
                    }
                    throw new IllegalArgumentException ("Wrong interactor class");
                })
                // check if any userList's backdrop image is missing
                .filter (chart -> chart.backDropPath == null)

                // download the backdrop image path
                .flatMapSingle (backDropFunction)
                .delay (300, TimeUnit.MILLISECONDS) // delay to avoid api limit
                // and then save
                .map (chart -> {
                    if ( database instanceof ChartsDatabaseInteractor ) {
                        return ((ChartsDatabaseInteractor) database).update (chart);
                    }
                    if ( database instanceof HostDatabaseInteractor ) {
                        return ((HostDatabaseInteractor) database).update (chart);
                    }
                    throw new IllegalArgumentException ("Wrong interactor class");
                })
                .subscribe (aLong -> {
                }, Throwable::printStackTrace);
    }

    public static String yearFromDateString (String releaseDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-mm-dd",
                Locale.getDefault ());

        GregorianCalendar gregorianCalendar = new GregorianCalendar ();
        try {
            Date date = simpleDateFormat.parse (releaseDate);
            gregorianCalendar.setTime (date);
        } catch ( ParseException e ) {
            e.printStackTrace ();
        }
        return String.valueOf (gregorianCalendar.get (Calendar.YEAR));

    }
}
