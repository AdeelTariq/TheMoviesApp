package com.winterparadox.themovieapp.charts;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.room.AppDatabase;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class ChartsPresenterImpl extends ChartsPresenter {

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    public ChartsPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (ChartsView view) {
        super.attachView (view);

        database.chartDao ()
                .getCharts ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (charts -> {
                    if ( view != null ) {
                        view.showCharts (charts);
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                    }
                    throwable.printStackTrace ();
                });
    }
}
