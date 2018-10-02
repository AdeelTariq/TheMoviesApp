package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.SearchResponse;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractor;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultApiInteractorImpl;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class SearchResultApiInteractorTest {

    @Mock SearchApiService searchApiService;

    SearchResultApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new SearchResultApiInteractorImpl (searchApiService);
    }

    @Test
    public void search_shouldReturnFromSearchService () {
        SearchResponse response = new SearchResponse ();
        response.results = new ArrayList<> ();
        response.results.add (new Movie ());
        response.results.add (new Movie ());

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        int page = 5;
        String query = "movie";
        given (searchApiService.search (anyString (), anyInt ()))
                .willReturn (Single.just (response));

        interactor.search (query, page).subscribe (subscriber);

        then (searchApiService).should ().search (query, page + 1);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (response.results);
    }
}
