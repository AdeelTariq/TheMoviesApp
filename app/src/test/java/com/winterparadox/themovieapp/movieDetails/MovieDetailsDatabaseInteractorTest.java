package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;
import com.winterparadox.themovieapp.common.room.UserListDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class MovieDetailsDatabaseInteractorTest {

    @Mock
    FavoriteDao favoriteDao;
    @Mock
    RecentlyViewedDao recentlyViewedDao;
    @Mock
    UserListDao userListDao;
    @Mock
    MovieDao movieDao;

    MovieDetailsDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new MovieDetailsDatabaseInteractorImpl (movieDao,
                recentlyViewedDao, favoriteDao, userListDao);
    }

    @Test
    public void addToCache_shouldReturnFromMovieDao () {
        Long id = 900L;
        given (movieDao.insertAll (any (Movie.class)))
                .willReturn (new Long[]{0L});

        TestObserver subscriber = new TestObserver<> ();

        Movie movie = new Movie ();
        movie.id = id;

        interactor.addToCache (movie).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (movieDao).should ().insertAll (movie);
        subscriber.assertComplete ();
    }

    @Test
    public void addToRecentlyViewed_shouldReturnFromRecentlyViewedDao () {
        Long id = 900L;
        given (recentlyViewedDao.insertAll (any (RecentlyViewed.class)))
                .willReturn (new Long[]{0L});

        TestObserver subscriber = new TestObserver<> ();

        Movie movie = new Movie ();
        movie.id = id;

        interactor.addToRecentlyViewed (movie).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (recentlyViewedDao).should ().insertAll (new RecentlyViewed (0, movie));
        subscriber.assertComplete ();
    }

    @Test
    public void addToFavorite_shouldReturnFromFavoriteDao () {
        Long id = 900L;
        given (favoriteDao.insertAll (any (Favorite.class)))
                .willReturn (new Long[]{0L});

        TestObserver subscriber = new TestObserver<> ();

        Movie movie = new Movie ();
        movie.id = id;

        interactor.addToFavorite (movie).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (favoriteDao).should ().insertAll (new Favorite (0, movie));
        subscriber.assertComplete ();
    }


    @Test
    public void unFavorite_shouldReturnFromFavoriteDao () {
        Long id = 900L;

        TestObserver subscriber = new TestObserver<> ();

        Movie movie = new Movie ();
        movie.id = id;

        interactor.unFavorite (movie).subscribe (subscriber);

        then (favoriteDao).should ().deleteAll (new Favorite (0, movie));
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertComplete ();
    }


    @Test
    public void isFavorite_shouldReturnFromFavoriteDao () {
        Long id = 900L;

        given (favoriteDao.isFavorite (anyLong ()))
                .willReturn (Single.just (true));

        TestObserver<Boolean> subscriber = new TestObserver<> ();

        Movie movie = new Movie ();
        movie.id = id;

        interactor.isFavorite (movie).subscribe (subscriber);

        then (favoriteDao).should ().isFavorite (movie.id);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (true);
    }


    @Test
    public void anyUserListExists_shouldReturnFromUserListDao () {
        given (userListDao.anyListExists ())
                .willReturn (Single.just (true));

        TestObserver<Boolean> subscriber = new TestObserver<> ();

        interactor.anyUserListExists ().subscribe (subscriber);

        then (userListDao).should ().anyListExists ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (true);
    }


    @Test
    public void getUserLists_shouldReturnFromUserListDao () {
        List<UserList> list = new ArrayList<> ();
        list.add (new UserList ());
        list.add (new UserList ());

        given (userListDao.getUserListsOnce ())
                .willReturn (Single.just (list));

        TestObserver<List<UserList>> subscriber = new TestObserver<> ();

        interactor.getUserLists ().subscribe (subscriber);

        then (userListDao).should ().getUserListsOnce ();
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (list);
    }

    @Test
    public void isMovieInList_shouldReturnFromUserListDao () {
        Long id = 900L;
        Long listId = 700L;
        given (userListDao.isInList (anyLong (), anyLong ()))
                .willReturn (true);

        boolean value = interactor.isMovieInList (id, listId);

        then (userListDao).should ().isInList (id, listId);

        assertTrue ("Wrong value returned", value);
    }

    @Test
    public void createDefaultUserLists_shouldReturnFromUserListDao () {
        given (userListDao.insertAllLists (any (UserList.class)))
                .willReturn (new Long[]{});

        List<String> list = new ArrayList<> ();
        list.add ("list1");
        list.add ("list2");
        UserList[] userLists = new UserList[2];
        userLists[0] = new UserList (list.get (0));
        userLists[1] = new UserList (list.get (1));

        interactor.createDefaultUserLists (list);

        then (userListDao).should ().insertAllLists (userLists);
    }
}
