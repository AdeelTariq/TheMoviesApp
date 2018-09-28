package com.winterparadox.themovieapp.dagger;


import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.charts.ChartsFragment;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListFragment;
import com.winterparadox.themovieapp.createList.CreateListDialogFragment;
import com.winterparadox.themovieapp.favorites.FavoritesFragment;
import com.winterparadox.themovieapp.home.HomeFragment;
import com.winterparadox.themovieapp.hostAndSearch.HostActivity;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultFragment;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsFragment;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogFragment;
import com.winterparadox.themovieapp.personDetails.PersonDetailsFragment;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedFragment;
import com.winterparadox.themovieapp.userLists.UserListsFragment;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogFragment;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Adeel on 8/3/2017.
 */

@Singleton
@Component(modules = {AppModule.class, ApiServiceModule.class, PresenterModule.class,
        ApiInteractorModule.class, ObjectsModule.class, DaoModule.class,
        DatabaseInteractorModule.class})
public interface AppComponent {

    void inject (App app);

    void inject (HostActivity target);

    void inject (HomeFragment target);

    void inject (MovieDetailsFragment target);

    void inject (RecentlyViewedFragment target);

    void inject (FavoritesFragment target);

    void inject (PersonDetailsFragment target);

    void inject (ChartsFragment target);

    void inject (ChartMovieListFragment target);

    void inject (UserListsFragment target);

    void inject (UserListDialogFragment target);

    void inject (UserMovieListFragment target);

    void inject (CreateListDialogFragment target);

    void inject (RenameListDialogFragment target);

    void inject (SearchResultFragment target);
}
