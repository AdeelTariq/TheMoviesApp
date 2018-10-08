package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecentlyViewedPresenterTest {

    private RecentlyViewedPresenter presenter;

    @Mock
    RecentlyViewedView view;

    @Mock
    Navigator navigator;

    @Mock
    RecentlyViewedDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new RecentlyViewedPresenterImpl (database, mainScheduler);
    }

    @Test
    public void attachView_shouldCallDatabase () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ());
        movies.add (new Movie ());
        movies.add (new Movie ());

        given (database.getRecentlyViewed (anyInt ()))
                .willReturn (Single.just (movies));

        presenter.attachView (view, navigator);

        verify (view).showProgress ();
        verify (database).getRecentlyViewed (200);
        verify (view).hideProgress ();
        verify (view).showMovies (movies, 0);
    }

    @Test
    public void saveState_shouldHaveCorrectIndex () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ());
        movies.add (new Movie ());
        movies.add (new Movie ());

        given (database.getRecentlyViewed (anyInt ()))
                .willReturn (Single.just (movies));

        presenter.attachView (view, navigator);

        presenter.saveState (9);
        presenter.detachView ();

        presenter.attachView (view, navigator);

        verify (view, times (2)).showProgress ();
        verify (database, times (2)).getRecentlyViewed (200);
        verify (view, times (2)).hideProgress ();
        verify (view).showMovies (movies, 0);
        verify (view).showMovies (movies, 9);
    }


    @Test
    public void onMovieClick_shouldCallNavigator () {
        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";


        given (database.getRecentlyViewed (anyInt ()))
                .willReturn (Single.just (new ArrayList<> ()));


        Object element = new Object ();

        presenter.attachView (view, navigator);

        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }
}
