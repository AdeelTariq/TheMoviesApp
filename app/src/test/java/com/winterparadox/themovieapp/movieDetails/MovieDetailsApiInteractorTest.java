package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Movie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class MovieDetailsApiInteractorTest {

    @Mock MovieDetailsApiService apiService;

    MovieDetailsApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new MovieDetailsApiInteractorImpl (apiService);
    }

    @Test
    public void movieDetails_shouldReturnFromMovieService () {
        Movie movie = new Movie (900L);

        TestObserver<Movie> subscriber = new TestObserver<> ();
        given (apiService.movieDetails (anyLong ()))
                .willReturn (Single.just (movie));

        interactor.movieDetails (movie.id).subscribe (subscriber);

        then (apiService).should ().movieDetails (movie.id);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (movie);
    }
}
