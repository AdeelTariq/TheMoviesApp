package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultPresenter;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultPresenterImpl;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultView;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SearchResultPresenterTest {

    private SearchResultPresenter presenter;

    @Mock
    SearchResultView view;

    @Mock
    Navigator navigator;

    @Mock
    SearchResultApiInteractor api;
    private String query = "movie";

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new SearchResultPresenterImpl (api, mainScheduler);
    }

    @Test
    public void saveState_attachViewShouldRestoreView () {

        presenter.attachView (view, query, navigator);


        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("2018-01-05"));
        movies.add (new Movie ("2018-03-04"));
        movies.add (new Movie ("2018-05-03"));
        movies.add (new Movie ("2018-06-02"));

        ArrayList<Object> items = new ArrayList<> (movies);

        presenter.saveState (2, items);
        presenter.detachView ();

        presenter.attachView (view, query, navigator);

        verify (view).restoreMovies (2, items, 0);
    }


    @Test
    public void search_shouldSearchDatabase () {

        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("2018-01-05"));
        movies.add (new Movie ("2018-03-04"));
        movies.add (new Movie ("2018-05-03"));
        movies.add (new Movie ("2018-06-02"));

        given (api.search (anyString (), anyInt ())).willReturn (Single.just (movies));

        presenter.attachView (view, query, navigator);
        presenter.fetchDataPage (0);
        presenter.fetchDataPage (3);

        verify (view, times (2)).showPageProgress ();
        verify (api).search (query, 0);
        verify (view).hideProgress ();
        verify (view).showMovies (movies);

        verify (api).search (query, 3);
        verify (view).hidePageProgress ();
        verify (view).addMovies (movies);
    }

    @Test
    public void retryClick_shouldCallNavigator () {

        presenter.attachView (view, query, navigator);
        presenter.onRetryClicked ();

        verify (navigator).openSearch ();
    }

    @Test
    public void movieSuggestionClick_shouldCallNavigator () {
        presenter.attachView (view, query, navigator);
        Movie movie = new Movie ();
        Object element = new Object ();
        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }

}
