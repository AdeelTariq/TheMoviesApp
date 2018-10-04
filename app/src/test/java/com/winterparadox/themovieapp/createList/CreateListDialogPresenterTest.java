package com.winterparadox.themovieapp.createList;

import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class CreateListDialogPresenterTest {

    private CreateListDialogPresenter presenter;

    @Mock
    CreateListDialogView view;

    @Mock
    CreateListDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        presenter = new CreateListDialogPresenterImpl (database);
    }

    @Test
    public void createList_shouldCallDatabase () {

        Long listId = 900L;

        given (database.createList (any ()))
                .willReturn (Single.just (listId));
        given (database.addToList (any ()))
                .willReturn (Completable.complete ());

        long movieId = 100L;
        String name = "List";

        presenter.createList (name, movieId);

        then (database).should ().createList (new UserList (name));
        then (database).should ().addToList (new UserListItem (listId, movieId));
    }
}
