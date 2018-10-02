package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListDatabaseInteractorImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class UserMovieListDatabaseInteractorTest {

    @Mock
    UserListDao dao;

    UserMovieListDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new UserMovieListDatabaseInteractorImpl (dao);
    }

    @Test
    public void deleteFromList_shouldReturnFromUserListDao () {

        TestObserver subscriber = new TestObserver ();

        long movieId = 500L;
        long listId = 700L;
        UserListItem userListItem = new UserListItem (listId, movieId);

        UserList userList = new UserList ();
        userList.id = listId;

        Movie movie = new Movie ();
        movie.id = movieId;

        interactor.deleteFromList (userList, movie).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().removeFromList (userListItem);
        subscriber.assertComplete ();
    }


    @Test
    public void saveListOrder_shouldReturnFromUserListDao () {

        TestObserver subscriber = new TestObserver ();

        long listId = 700L;

        UserList userList = new UserList ();
        userList.id = listId;

        long movieId1 = 300L;
        long movieId2 = 500L;
        long movieId3 = 700L;

        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie (movieId1));
        movies.add (new Movie (movieId2));
        movies.add (new Movie (movieId3));


        UserListItem[] userListItems = new UserListItem[3];
        userListItems[0] = new UserListItem (listId, movieId1);
        userListItems[1] = new UserListItem (listId, movieId2);
        userListItems[2] = new UserListItem (listId, movieId3);

        interactor.saveListOrder (userList, movies).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().addAllToList (userListItems);
        subscriber.assertComplete ();
    }


    @Test
    public void getListMovies_shouldReturnFromUserListDao () {
        List<Movie> movies = new ArrayList<> ();

        long movieId1 = 300L;
        long movieId2 = 500L;
        long movieId3 = 700L;


        movies.add (new Movie (movieId1));
        movies.add (new Movie (movieId2));
        movies.add (new Movie (movieId3));

        given (dao.getListMovies (anyLong ()))
                .willReturn (Single.just (movies));

        TestObserver<List<Movie>> subscriber = new TestObserver<> ();

        long listId = 700L;

        UserList userList = new UserList ();
        userList.id = listId;

        interactor.getListMovies (userList).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().getListMovies (listId);
        subscriber.assertValue (movies);
    }
}
