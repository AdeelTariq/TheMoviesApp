package com.winterparadox.themovieapp.search;

import com.winterparadox.themovieapp.room.AppDatabase;

import io.reactivex.Scheduler;

public class HostPresenterImpl extends HostPresenter {

    private final AppDatabase database;
    private final Scheduler mainScheduler;

    public HostPresenterImpl (AppDatabase database, Scheduler mainScheduler) {

        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (HostView view) {
        super.attachView (view);


    }
}
