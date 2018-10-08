package com.winterparadox.themovieapp.movieDetails;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.GenresItem;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.common.beans.RegionItem;
import com.winterparadox.themovieapp.common.beans.ReleaseDatesItem;
import com.winterparadox.themovieapp.common.beans.UserList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsPresenterImpl extends MovieDetailsPresenter {

    private static final String US = "US";
    private final MovieDetailsApiInteractor api;
    private final Scheduler mainScheduler;
    private MovieDetailsDatabaseInteractor database;
    private boolean isFavorite;

    public MovieDetailsPresenterImpl (MovieDetailsApiInteractor api,
                                      Scheduler mainScheduler,
                                      MovieDetailsDatabaseInteractor database) {
        this.api = api;
        this.mainScheduler = mainScheduler;
        this.database = database;
    }

    @SuppressLint("CheckResult")
    @Override
    void attachView (MovieDetailsView view, Movie movie, Navigator nav) {
        super.attachView (view, movie, nav);

        database.addToCache (movie).andThen (database.addToRecentlyViewed (movie))
                .subscribe ();

        database.isFavorite (movie)
                .observeOn (mainScheduler)
                .subscribe (isFav -> {
                    isFavorite = isFav;
                    if ( view != null ) {
                        view.showFavorite (isFavorite);
                    }
                });


        view.showMovie (movie, String.format (Locale.getDefault (),
                "(%s)", PresenterUtils.yearFromDateString (movie.releaseDate)));
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchAdditionalDetails () {
        view.showProgress ();
        api.movieDetails (movie.id)
                .observeOn (mainScheduler)
                .subscribe (movie1 -> {
                    movie = movie1;

                    if ( movie.credits.cast.size () > 20 ) {
                        movie.credits.cast = movie.credits.cast.subList (0, 20);
                    }
                    if ( movie.credits.crew.size () > 20 ) {
                        movie.credits.crew = movie.credits.crew.subList (0, 20);
                    }

                    if ( view != null ) {

                        String ageRating = "NA";
                        for ( RegionItem region : movie1.releaseDates.results ) {
                            if ( US.equalsIgnoreCase (region.iso31661) ) {
                                for ( ReleaseDatesItem releaseDate : region.releaseDates ) {
                                    if ( !releaseDate.certification.isEmpty () ) {
                                        ageRating = releaseDate.certification;
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        String genres = "";
                        for ( GenresItem genre : movie1.genres ) {
                            genres += genre.name + ", ";
                        }
                        if ( genres.endsWith (", ") ) {
                            genres = genres.substring (0, genres.length () - 2);
                        }

                        view.showAdditionalDetails (movie, ageRating,
                                PresenterUtils.runtimeString (movie1.runtime), genres);
                        view.hideProgress ();
                    }
                }, throwable -> {
                    if ( view != null ) {
                        view.hideProgress ();
                        view.showError (throwable.getMessage ());
                    }
                    throwable.printStackTrace ();
                });
    }

    @Override
    boolean isMovieFav () {
        return isFavorite;
    }

    @Override
    void setMovieFav (boolean isFav) {
        isFavorite = isFav;
        // save to database
        if ( isFavorite ) {
            database.addToFavorite (movie)
                    .observeOn (mainScheduler)
                    .subscribe ();
        } else {
            database.unFavorite (movie)
                    .observeOn (mainScheduler)
                    .subscribe ();
        }
    }

    @Override
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }

    @Override
    public void onPersonClicked (Person member, Object view) {
        if ( navigator != null ) {
            navigator.openPerson (member, view);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onAddToListClicked () {
        database.anyUserListExists ()
                .map (anyExists -> {
                    if ( !anyExists ) {
                        if ( view != null ) {
                            List<String> defaultLists = view.getDefaultLists ();
                            database.createDefaultUserLists (defaultLists);
                        }
                    }
                    return anyExists;
                })
                .flatMap (b -> database.getUserLists ())
                .toObservable ()
                .flatMapIterable (userLists -> userLists)
                .map (userList -> {
                    userList.isAdded = database.isMovieInList (movie.id, userList.id);
                    return userList;
                })
                .toList ()
                .subscribeOn (Schedulers.io ())
                .observeOn (mainScheduler)
                .subscribe (userLists -> {
                    if ( navigator != null ) {
                        ArrayList<UserList> objects = new ArrayList<> (userLists);
                        navigator.openListSelector (objects, movie.id);
                    }
                });
    }
}
