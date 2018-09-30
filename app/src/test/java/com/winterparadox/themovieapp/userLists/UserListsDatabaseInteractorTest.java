package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.room.UserListDao;

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
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class UserListsDatabaseInteractorTest {

    @Mock
    UserListDao dao;

    UserListsDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new UserListsDatabaseInteractorImpl (dao);
    }

    @Test
    public void anyUserListExists_shouldReturnFromUserListDao () {
        given (dao.anyListExists ())
                .willReturn (Single.just (true));

        TestObserver<Boolean> subscriber = new TestObserver<> ();

        interactor.anyUserListExists ().subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().anyListExists ();
        subscriber.assertValue (true);
    }


    @Test
    public void getUserLists_shouldReturnFromUserListDao () {
        List<UserList> list = new ArrayList<> ();
        list.add (new UserList ());
        list.add (new UserList ());

        given (dao.getUserLists ())
                .willReturn (Flowable.just (list));

        TestSubscriber<List<UserList>> subscriber = new TestSubscriber<> ();

        interactor.getUserLists ().subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().getUserLists ();
        subscriber.assertValue (list);
    }

    @Test
    public void createDefaultUserLists_shouldReturnFromUserListDao () {
        given (dao.insertAllLists (any (UserList.class)))
                .willReturn (new Long[]{});

        List<String> list = new ArrayList<> ();
        list.add ("list1");
        list.add ("list2");
        UserList[] userLists = new UserList[2];
        userLists[0] = new UserList (list.get (0));
        userLists[1] = new UserList (list.get (1));

        interactor.createDefaultUserLists (list);

        then (dao).should ().insertAllLists (userLists);
    }

    @Test
    public void isListEmpty_shouldReturnFromUserListDao () {
        UserList userList = new UserList ();
        Long listId = 700L;
        userList.id = listId;

        given (dao.anyItemExists (anyLong ()))
                .willReturn (false);

        boolean value = interactor.isListEmpty (userList);

        then (dao).should ().anyItemExists (listId);

        assertTrue ("Wrong value returned", value);
    }

    @Test
    public void getTopMovieFromList_shouldReturnFromUserListDao () {
        UserList userList = new UserList ();
        Long listId = 700L;
        userList.id = listId;

        Movie movie = new Movie ();

        given (dao.getTopListMovie (anyLong ()))
                .willReturn (Single.just (movie));

        Movie value = interactor.getTopMovieFromList (userList);

        then (dao).should ().getTopListMovie (listId);

        assertEquals ("Wrong value returned", movie, value);
    }

    @Test
    public void duplicateListItems_shouldReturnFromUserListDao () {
        UserList userList = new UserList ();
        userList.id = 700L;

        TestObserver subscriber = new TestObserver<> ();

        interactor.duplicateListItems (userList).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().duplicateListItems (userList);
        subscriber.assertComplete ();
    }

    @Test
    public void deleteList_shouldReturnFromUserListDao () {
        UserList userList = new UserList ();
        userList.id = 700L;

        TestObserver subscriber = new TestObserver<> ();

        interactor.deleteList (userList).subscribe (subscriber);

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().deleteListItems (userList.id);
        then (dao).should ().deleteList (userList);
        subscriber.assertComplete ();
    }

}
