package com.winterparadox.themovieapp.common;

import android.support.annotation.NonNull;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.room.AppDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    public static Disposable createChartss (Single<List<Chart>> generes,
                                            Function<Chart, SingleSource<? extends Chart>>
                                                    backDropFunction,
                                            AppDatabase database,
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
                    database.chartDao ().insert (chart);
                    return database.chartDao ().getChart (chart.id);
                })
                // check if any chart's backdrop image is missing
                .filter (chart -> chart.backDropPath == null)

                // download the backdrop image path
                .flatMapSingle (backDropFunction)
                .delay (300, TimeUnit.MILLISECONDS) // delay to avoid api limit
                // and then save
                .map (chart -> database.chartDao ().update (chart))

                .subscribe (aLong -> {
                }, Throwable::printStackTrace);
    }
}
