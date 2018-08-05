package com.winterparadox.themovieapp.movieDetails;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.GenresItem;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RegionItem;
import com.winterparadox.themovieapp.common.beans.ReleaseDatesItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.reactivex.Scheduler;

public class MovieDetailsPresenterImpl extends MovieDetailsPresenter {

    private static final String US = "US";
    private final MovieDetailsApiInteractor api;
    private final Scheduler mainScheduler;

    public MovieDetailsPresenterImpl (MovieDetailsApiInteractor api,
                                      Scheduler mainScheduler) {
        this.api = api;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachView (MovieDetailsView view, Movie movie) {
        super.attachView (view, movie);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-mm-dd",
                Locale.getDefault ());

        GregorianCalendar gregorianCalendar = new GregorianCalendar ();
        try {
            Date date = simpleDateFormat.parse (movie.releaseDate);
            gregorianCalendar.setTime (date);
        } catch ( ParseException e ) {
            e.printStackTrace ();
        }
        view.showMovie (movie, String.format (Locale.getDefault (),
                "(%d)", gregorianCalendar.get (Calendar.YEAR)));

        fetchAdditionalDetails ();
    }

    @SuppressLint("CheckResult")
    private void fetchAdditionalDetails () {
        view.showProgress ();
        api.movieDetails (movie.id)
                .observeOn (mainScheduler)
                .subscribe (movie1 -> {
                    movie = movie1;

                    if ( movie.credits.cast.size () > 10 ) {
                        movie.credits.cast = movie.credits.cast.subList (0, 10);
                    }
                    if ( movie.credits.crew.size () > 10 ) {
                        movie.credits.crew = movie.credits.crew.subList (0, 10);
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
}
