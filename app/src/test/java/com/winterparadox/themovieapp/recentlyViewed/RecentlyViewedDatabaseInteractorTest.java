package com.winterparadox.themovieapp.recentlyViewed;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class RecentlyViewedDatabaseInteractorTest {

    @Mock
    RecentlyViewedDao dao;

    RecentlyViewedDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new RecentlyViewedDatabaseInteractorImpl (dao);
    }

    @Test
    public void getRecentlyViewed_shouldReturnFromRecentlyViewedDao () {
        List<Movie> list = new ArrayList<> ();
        list.add (new Movie ());
        list.add (new Movie ());
        list.add (new Movie ());

        given (dao.getRecent (anyInt ()))
                .willReturn (Single.just (list));

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        int count = 50;

        interactor.getRecentlyViewed (count).subscribe (subscriber);

        then (dao).should ().getRecent (count);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }
}
