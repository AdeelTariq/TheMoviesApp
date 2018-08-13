package com.winterparadox.themovieapp.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.winterparadox.themovieapp.common.room.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Adeel on 8/8/2017.
 */
@Module
public class ObjectsModule {

    @Provides
    @Singleton
    Gson provideGson () {
        return new Gson ();
    }

    @Singleton
    @Provides
    public Scheduler providesMainScheduler () {
        return AndroidSchedulers.mainThread ();
    }

    @Singleton
    @Provides
    public AppDatabase providesAppDatabase (Context context) {
        return Room.databaseBuilder (context,
                AppDatabase.class, "movieDatabase").build ();
    }
}
