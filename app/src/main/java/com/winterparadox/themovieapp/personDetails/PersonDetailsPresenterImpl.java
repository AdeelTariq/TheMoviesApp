package com.winterparadox.themovieapp.personDetails;

import android.annotation.SuppressLint;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.PresenterUtils;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Scheduler;

public class PersonDetailsPresenterImpl extends PersonDetailsPresenter {

    private final PersonApiInteractor api;
    private final Scheduler mainScheduler;

    public PersonDetailsPresenterImpl (PersonApiInteractor api, Scheduler mainScheduler) {

        this.api = api;
        this.mainScheduler = mainScheduler;
    }

    @SuppressLint("CheckResult")
    @Override
    public void attachView (PersonDetailsView v, Person person, Navigator navigator) {
        super.attachView (v, person, navigator);

        view.showProgress ();

        api.personDetails (person.id)
                .observeOn (mainScheduler)
                .subscribe (person1 -> {
                    if ( view != null ) {

                        // gather all credits and sort to get top credits
                        Set<Movie> allCredits = new HashSet<> (person1.credits.crew);
                        allCredits.addAll (person1.credits.cast);

                        // sorting by rating first and then by release date
                        List<Movie> sorted = PresenterUtils.asSortedList (allCredits, (t, t1) -> {
                            int compare = Integer.compare ((int) t1.popularity,
                                    (int) t.popularity);

                            if ( compare != 0 ) {
                                return compare;
                            }

                            return String.valueOf (t.releaseDate)
                                    .compareTo (String.valueOf (t1.releaseDate));
                        });

                        List<Movie> sublist = sorted;
                        int limit = 20;
                        if ( sorted.size () > limit ) {
                            sublist = sorted.subList (0, limit);
                        }

                        view.showAdditionalDetails (person1.bio, person1.knownFor,
                                String.valueOf (sorted.size ()), sublist);

                        if ( !person1.images.all.isEmpty () ) {
                            view.showImages (person1.images.all);
                        }

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
    public void onMovieClicked (Movie movie, Object element) {
        if ( navigator != null ) {
            navigator.openMovie (movie, element);
        }
    }
}
