package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class CreateListDatabaseInteractorTest {

    @Mock
    UserListDao dao;

    CreateListDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new CreateListDatabaseInteractorImpl (dao);
    }

    @Test
    public void createList_shouldReturnFromUserListDao () {
        long value = 9000L;
        given (dao.insertList (any (UserList.class)))
                .willReturn (value);

        TestObserver<Long> subscriber = new TestObserver<> ();

        UserList userList = new UserList ();

        interactor.createList (userList).subscribe (subscriber);

        subscriber.awaitDone (500, TimeUnit.MILLISECONDS);
        then (dao).should ().insertList (userList);
        subscriber.assertResult (value);
    }

    @Test
    public void addToList_shouldReturnFromUserListDao () {
        long value = 9000L;
        given (dao.addToList (any (UserListItem.class)))
                .willReturn (value);

        UserListItem userListItem = new UserListItem ();

        interactor.addToList (userListItem).subscribe ();

        then (dao).should ().addToList (userListItem);
    }
}
