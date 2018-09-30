package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
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

public class HomeDatabaseInteractorTest {

    @Mock
    FavoriteDao favoriteDao;

    @Mock
    RecentlyViewedDao recentlyViewedDao;

    HomeDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new HomeDatabaseInteractorImpl (favoriteDao, recentlyViewedDao);
    }

    @Test
    public void getHomeFavorites_shouldReturnFromFavoritesDao () {
        List<Movie> list = new ArrayList<> ();
        list.add (new Movie ());
        list.add (new Movie ());

        given (favoriteDao.getHomeFavorites (anyInt ()))
                .willReturn (Single.just (list));

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        interactor.getHomeFavorites ().subscribe (subscriber);

        then (favoriteDao).should ().getHomeFavorites (4);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }


    @Test
    public void getHomeRecents_shouldReturnFromRecentlyViewedDao () {
        List<Movie> list = new ArrayList<> ();
        list.add (new Movie ());
        list.add (new Movie ());
        list.add (new Movie ());

        given (recentlyViewedDao.getRecent (anyInt ()))
                .willReturn (Single.just (list));

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        interactor.getHomeRecents ().subscribe (subscriber);

        then (recentlyViewedDao).should ().getRecent (4);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }

}
