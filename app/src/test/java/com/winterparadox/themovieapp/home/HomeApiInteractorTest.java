package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.MoviesResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class HomeApiInteractorTest {

    @Mock
    HomeApiService homeApiService;

    HomeApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new HomeApiInteractorImpl (homeApiService);
    }

    @Test
    public void popularMovies_shouldReturnFromHomeService () {
        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (homeApiService.popularMovies ()).willReturn (Single.just (response));

        interactor.popularMovies ().subscribe (subscriber);

        then (homeApiService).should ().popularMovies ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (response.results);
    }

    @Test
    public void upcomingMovies_shouldReturnFromHomeService () {
        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (homeApiService.upcomingMovies ()).willReturn (Single.just (response));

        interactor.upcomingMovies ().subscribe (subscriber);

        then (homeApiService).should ().upcomingMovies ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (response.results);
    }
}
