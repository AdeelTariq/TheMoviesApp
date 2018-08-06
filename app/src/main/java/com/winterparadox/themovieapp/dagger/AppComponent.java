package com.winterparadox.themovieapp.dagger;


import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.favorites.FavoritesFragment;
import com.winterparadox.themovieapp.home.HomeFragment;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsFragment;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedFragment;
import com.winterparadox.themovieapp.search.HostActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Adeel on 8/3/2017.
 */

@Singleton
@Component(modules = {AppModule.class, ApiServiceModule.class, PresenterModule.class,
        InteractorModule.class, ObjectsModule.class})
public interface AppComponent {

    void inject (App app);

    void inject (HostActivity target);

    void inject (HomeFragment target);

    void inject (MovieDetailsFragment target);

    void inject (RecentlyViewedFragment target);

    void inject (FavoritesFragment target);
}
