package com.winterparadox.themovieapp.userLists.renameList;

import com.winterparadox.themovieapp.common.beans.UserList;
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

public class RenameListDatabaseInteractorTest {

    @Mock
    UserListDao dao;

    RenameListDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new RenameListDatabaseInteractorImpl (dao);
    }

    @Test
    public void renameList_shouldReturnFromUserListDao () {

        given (dao.updateList (any (UserList.class)))
                .willReturn (0L);

        TestObserver subscriber = new TestObserver ();

        UserList userList = new UserList ();
        userList.name = "old name";
        String newName = "new name";

        interactor.renameList (newName, userList)
                .subscribe (subscriber);

        UserList userListUpdated = new UserList ();
        userListUpdated.name = newName;

        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        then (dao).should ().updateList (userListUpdated);
        subscriber.assertComplete ();
    }
}
