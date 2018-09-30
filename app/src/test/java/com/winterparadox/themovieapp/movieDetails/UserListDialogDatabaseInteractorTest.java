package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogDatabaseInteractor;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogDatabaseInteractorImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class UserListDialogDatabaseInteractorTest {

    @Mock
    UserListDao dao;

    UserListDialogDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new UserListDialogDatabaseInteractorImpl (dao);
    }

    @Test
    public void addToList_shouldReturnFromUserListDao () {
        long movieId = 9000L;
        given (dao.addToList (any (UserListItem.class)))
                .willReturn (0L);

        TestObserver subscriber = new TestObserver ();

        UserList userList = new UserList ();
        userList.id = 700L;

        interactor.addToList (userList, movieId).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().addToList (new UserListItem (userList.id, movieId));
        subscriber.assertComplete ();
    }

    @Test
    public void removeFromList_shouldReturnFromUserListDao () {
        long movieId = 9000L;

        TestObserver subscriber = new TestObserver ();

        UserList userList = new UserList ();
        userList.id = 700L;

        interactor.removeFromList (userList, movieId).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().removeFromList (new UserListItem (userList.id, movieId));
        subscriber.assertComplete ();
    }
}
