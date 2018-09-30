package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.ChartDao;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class HostDatabaseInteractorTest {

    @Mock
    FavoriteDao favoriteDao;
    @Mock
    RecentlyViewedDao recentlyViewedDao;
    @Mock
    ChartDao chartDao;
    @Mock
    MovieDao movieDao;

    HostDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new HostDatabaseInteractorImpl (chartDao, movieDao,
                favoriteDao, recentlyViewedDao);
    }

    @Test
    public void insertChart_shouldReturnFromChartsDao () {
        Long id = 900L;
        given (chartDao.insert (any (Chart.class))).willReturn (id);

        Chart chart = new Chart ();

        Long aLong = interactor.insertChart (chart);

        then (chartDao).should ().insert (chart);

        assertEquals ("Wrong id returned", id, aLong);
    }

    @Test
    public void updateChart_shouldReturnFromChartsDao () {
        Long id = 900L;
        given (chartDao.update (any (Chart.class))).willReturn (id);

        Chart chart = new Chart ();

        Long aLong = interactor.updateChart (chart);

        then (chartDao).should ().update (chart);

        assertEquals ("Wrong id returned", id, aLong);
    }


    @Test
    public void getChart_shouldReturnFromChartsDao () {
        long id = 56;
        Chart chart = new Chart ();
        chart.id = id;

        given (chartDao.getChart (anyLong ()))
                .willReturn (Single.just (chart));

        TestObserver<Chart> subscriber = new TestObserver<> ();

        interactor.getChart (id).subscribe (subscriber);

        then (chartDao).should ().getChart (id);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (chart);
    }

    @Test
    public void getSuggestions_shouldReturnFromMovieDao () {
        List<Movie> list = new ArrayList<> ();
        list.add (new Movie ());
        list.add (new Movie ());

        given (movieDao.search (anyString (), anyInt ()))
                .willReturn (Single.just (list));

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        String query = "movie name";

        interactor.getSuggestions (query).subscribe (subscriber);

        then (movieDao).should ().search ("%" + query + "%", 10);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }


    @Test
    public void anyFavoriteExists_shouldReturnFromFavoriteDao () {

        given (favoriteDao.anyExists ()).willReturn (Flowable.just (true));

        TestSubscriber<Boolean> subscriber = new TestSubscriber<> ();

        interactor.anyFavoriteExists ().subscribe (subscriber);

        then (favoriteDao).should ().anyExists ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (true);
    }

    @Test
    public void anyRecentlyViewedExists_shouldReturnFromRecentlyViewedDao () {

        given (recentlyViewedDao.anyExists ()).willReturn (Flowable.just (true));

        TestSubscriber<Boolean> subscriber = new TestSubscriber<> ();

        interactor.anyRecentyViewedExists ().subscribe (subscriber);

        then (recentlyViewedDao).should ().anyExists ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (true);
    }

}
