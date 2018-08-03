package com.winterparadox.themovieapp.dagger;

import com.winterparadox.themovieapp.home.HomeApiInteractor;
import com.winterparadox.themovieapp.home.HomePresenter;
import com.winterparadox.themovieapp.home.HomePresenterImpl;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by Adeel on 10/1/2017.
 */

@Module
public class PresenterModule {

    @Provides
    public HomePresenter provideHomePresenter (HomeApiInteractor apiInteractor, Scheduler
            mainScheduler) {
        return new HomePresenterImpl (apiInteractor, mainScheduler);
    }

}
