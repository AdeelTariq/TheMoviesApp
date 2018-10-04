package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.apiServices.ConfigurationApiService;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.GenresResponse;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class ChartsApiInteractorTest {

    @Mock
    ChartsApiService chartsApiService;
    @Mock
    ConfigurationApiService configurationApiService;

    ChartsApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new ChartsApiInteractorImpl (configurationApiService, chartsApiService);
    }

    @Test
    public void genres_shouldReturnFromConfigService () {
        GenresResponse response = new GenresResponse ();
        response.genres = new ArrayList<> ();

        TestObserver<List<Chart>> subscriber = new TestObserver<> ();
        given (configurationApiService.genres ()).willReturn (Single.just (response));

        interactor.genres ().subscribe (subscriber);

        then (configurationApiService).should ().genres ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (response.genres);
    }

    @Test
    public void popularMovieBackdrop_shouldReturnFromChartsService () {
        Chart chart = new Chart ();

        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.backdropPath = "image_path";
        response.results.add (movie);

        TestObserver<Chart> subscriber = new TestObserver<> ();
        given (chartsApiService.popular (anyInt ())).willReturn (Single.just (response));

        interactor.popularMovieBackdrop (chart).subscribe (subscriber);

        then (chartsApiService).should ().popular (1);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (chart);
    }

    @Test
    public void latestMovieBackdrop_shouldReturnFromChartsService () {
        Chart chart = new Chart ();

        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.backdropPath = "image_path";
        response.results.add (movie);

        TestObserver<Chart> subscriber = new TestObserver<> ();
        given (chartsApiService.latest (anyInt ())).willReturn (Single.just (response));

        interactor.latestMovieBackdrop (chart).subscribe (subscriber);

        then (chartsApiService).should ().latest (1);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (chart);
    }

    @Test
    public void topMovieBackdrop_shouldReturnFromChartsService () {
        Chart chart = new Chart ();

        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.backdropPath = "image_path";
        response.results.add (movie);

        TestObserver<Chart> subscriber = new TestObserver<> ();
        given (chartsApiService.topRated (anyInt ())).willReturn (Single.just (response));

        interactor.topRatedMovieBackdrop (chart).subscribe (subscriber);

        then (chartsApiService).should ().topRated (1);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (chart);
    }

    @Test
    public void topMovieInGenreBackdrop_shouldReturnFromChartsService () {
        long CHART_ID = 500;
        Chart chart = new Chart ();
        chart.id = CHART_ID;

        MoviesResponse response = new MoviesResponse ();
        response.results = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.backdropPath = "image_path";
        response.results.add (movie);

        TestObserver<Chart> subscriber = new TestObserver<> ();
        given (chartsApiService.topRatedInGenre (anyLong (), anyInt ()))
                .willReturn (Single.just (response));

        interactor.genreMovieBackdrop (chart).subscribe (subscriber);

        then (chartsApiService).should ().topRatedInGenre (CHART_ID, 1);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (chart);
    }

}
