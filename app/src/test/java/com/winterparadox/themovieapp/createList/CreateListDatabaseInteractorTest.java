package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.UserListDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
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

        UserList userList = new UserList ();

        long returnedId = interactor.createList (userList);

        then (dao).should ().insertList (userList);
        assertEquals ("Wrong id returned", value, returnedId);
    }

    @Test
    public void addToList_shouldReturnFromUserListDao () {
        long value = 9000L;
        given (dao.addToList (any (UserListItem.class)))
                .willReturn (value);

        UserListItem userListItem = new UserListItem ();

        long returnedId = interactor.addToList (userListItem);

        then (dao).should ().addToList (userListItem);
        assertEquals ("Wrong id returned", value, returnedId);
    }
}
