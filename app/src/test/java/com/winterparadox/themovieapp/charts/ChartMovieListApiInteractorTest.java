package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListApiInteractor;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListApiInteractorImpl;
import com.winterparadox.themovieapp.common.apiServices.ChartsApiService;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.MoviesResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class ChartMovieListApiInteractorTest {

    @Mock
    ChartsApiService chartsApiService;

    ChartMovieListApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new ChartMovieListApiInteractorImpl (chartsApiService);
    }

    @Test
    public void topRatedMovieList_shouldReturnFromChartsService () {
        int PAGE = 6;

        MoviesResponse response = new MoviesResponse ();
        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (chartsApiService.topRated (anyInt ()))
                .willReturn (Single.just (response));

        interactor.topRatedMovies (PAGE).subscribe (subscriber);

        then (chartsApiService).should ().topRated (PAGE + 1);
    }

    @Test
    public void latestMovieList_shouldReturnFromChartsService () {
        int PAGE = 900;

        MoviesResponse response = new MoviesResponse ();
        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (chartsApiService.latest (anyInt ()))
                .willReturn (Single.just (response));

        interactor.latestMovies (PAGE).subscribe (subscriber);

        then (chartsApiService).should ().latest (PAGE + 1);
    }

    @Test
    public void popularMovieList_shouldReturnFromChartsService () {
        int PAGE = 6;

        MoviesResponse response = new MoviesResponse ();
        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (chartsApiService.popular (anyInt ()))
                .willReturn (Single.just (response));

        interactor.popularMovies (PAGE).subscribe (subscriber);

        then (chartsApiService).should ().popular (PAGE + 1);
    }

    @Test
    public void topMovieListInGenre_shouldReturnFromChartsService () {
        long CHART_ID = 500;
        int PAGE = 4;

        MoviesResponse response = new MoviesResponse ();
        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (chartsApiService.topRatedInGenre (anyLong (), anyInt ()))
                .willReturn (Single.just (response));

        interactor.topRatedInGenre (CHART_ID, PAGE).subscribe (subscriber);

        then (chartsApiService).should ().topRatedInGenre (CHART_ID, PAGE + 1);
    }

}
