package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.FavoriteDao;

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

public class FavoritesDatabaseInteractorTest {

    @Mock
    FavoriteDao dao;

    FavoritesDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new FavoritesDatabaseInteractorImpl (dao);
    }

    @Test
    public void getFavorites_shouldReturnFromFavoritesDao () {
        List<Movie> list = new ArrayList<> ();
        list.add (new Movie ());
        list.add (new Movie ());
        list.add (new Movie ());

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();
        given (dao.getFavorites ()).willReturn (Single.just (list));

        interactor.getFavorites ().subscribe (subscriber);

        then (dao).should ().getFavorites ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }

    @Test
    public void unFavorite_shouldReturnFromFavoritesDao () {
        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        long id = 9000L;
        Movie movie = new Movie ();
        movie.id = id;

        interactor.unFavorite (movie).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().deleteAll (new Favorite (0, movie));
        subscriber.assertComplete ();
    }
}
