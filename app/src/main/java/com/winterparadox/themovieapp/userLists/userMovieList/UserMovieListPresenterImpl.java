package com.winterparadox.themovieapp.userLists.userMovieList;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;

public class UserMovieListPresenterImpl extends UserMovieListPresenter {

    private Scheduler mainScheduler;

    private UserMovieListDatabaseInteractor database;

    public UserMovieListPresenterImpl (UserMovieListDatabaseInteractor database,
                                       Scheduler mainScheduler) {
        this.database = database;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (UserMovieListView view, UserList list, Navigator navigator) {
        super.attachView (view, list, navigator);

        fetchData ();
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchData () {
        database.getListMovies (userList)
                .observeOn (mainScheduler)
                .subscribe (movies -> {
                    if ( view != null ) {
                        for ( Movie movie : movies ) {
                            movie.year = PresenterUtils.yearFromDateString (movie.releaseDate);
                        }
                        view.showMovies (movies);
                        view.hideProgress ();
                    }

                }, throwable -> {
                    if ( view != null ) {
                        view.showError (throwable.getMessage ());
                        view.hideProgress ();
                    }
                    throwable.printStackTrace ();
                });

    }

    @Override
    public void deleteMovie (Movie movie) {
        database.deleteFromList (userList, movie).subscribe ();
    }

    @Override
    public void saveListOrder (List<Object> movies) {
        ArrayList<Movie> list = new ArrayList<> ();
        for ( Object movie : movies ) {
            if ( !(movie instanceof Movie) ) {
                continue;
            }
            list.add (((Movie) movie));
        }
        database.saveListOrder (userList, list).subscribe ();
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }

    @Override
    public void onDiscoverClick () {
        if ( navigator != null ) {
            navigator.openCharts ();
        }
    }
}
