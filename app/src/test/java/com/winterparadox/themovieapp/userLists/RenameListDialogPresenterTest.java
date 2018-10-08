package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogPresenter;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogPresenterImpl;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class RenameListDialogPresenterTest {

    private RenameListDialogPresenter presenter;

    @Mock
    RenameListDialogView view;

    @Mock
    Navigator navigator;

    @Mock
    RenameListDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        presenter = new RenameListDialogPresenterImpl (database);
    }

    @Test
    public void renameList_shouldCallDatabase () {

        given (database.renameList (anyString (), any ()))
                .willReturn (Completable.complete ());

        presenter.attachView (view, navigator);

        UserList userList = new UserList ();
        String name = "namee";

        presenter.renameList (name, userList);

        verify (database).renameList (name, userList);
    }
}
