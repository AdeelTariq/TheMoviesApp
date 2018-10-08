package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Image;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class PersonDetailsPresenterTest {

    private PersonDetailsPresenter presenter;

    @Mock
    PersonDetailsView view;

    @Mock
    Navigator navigator;

    @Mock
    PersonApiInteractor api;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new PersonDetailsPresenterImpl (api, mainScheduler);
    }

    @Test
    public void attachView_shouldCallDatabase () {
        Person person = new Person ();
        person.id = 100L;
        person.credits = new Person.MovieCredits ();
        person.credits.cast = new ArrayList<> ();
        person.credits.crew = new ArrayList<> ();
        person.images = new Person.TaggedImages ();
        person.images.all = new ArrayList<> ();
        person.images.all.add (new Image ());

        given (api.personDetails (anyLong ())).willReturn (Single.just (person));

        presenter.attachView (view, person, navigator);

        verify (view).showProgress ();
        verify (api).personDetails (person.id);
        verify (view).showAdditionalDetails (any (), any (), any (), any ());
        verify (view).showImages (person.images.all);
        verify (view).hideProgress ();
    }

    @Test
    public void onMovieClick_shouldCallNavigator () {
        Person person = new Person ();
        person.id = 100L;
        person.credits = new Person.MovieCredits ();
        person.credits.cast = new ArrayList<> ();
        person.credits.crew = new ArrayList<> ();
        person.images = new Person.TaggedImages ();
        person.images.all = new ArrayList<> ();
        person.images.all.add (new Image ());

        given (api.personDetails (anyLong ())).willReturn (Single.just (person));


        Movie movie = new Movie (100L, "movie");
        Object element = new Object ();

        presenter.attachView (view, person, navigator);

        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }
}
