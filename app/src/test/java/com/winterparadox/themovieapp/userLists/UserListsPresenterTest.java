package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserListsPresenterTest {

    private UserListsPresenter presenter;

    @Mock
    UserListsView view;

    @Mock
    Navigator navigator;

    @Mock
    UserListsDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new UserListsPresenterImpl (database, mainScheduler);
    }

    @Test
    public void attachView_shouldCallDatabase () throws InterruptedException {

        given (database.anyUserListExists ())
                .willReturn (Single.just (false));

        List<String> defaultLists = new ArrayList<> ();
        defaultLists.add ("Watched");
        defaultLists.add ("Watching");
        defaultLists.add ("Collection");

        given (view.getDefaultLists ()).willReturn (defaultLists);

        List<UserList> userLists = new ArrayList<> ();
        userLists.add (new UserList (defaultLists.get (0)));
        userLists.add (new UserList (defaultLists.get (1)));
        userLists.add (new UserList (defaultLists.get (2)));

        String BACKDROPPATH = "backdrop";

        ArrayList<UserList> userLists1 = new ArrayList<> (userLists);
        userLists1.get (0).backDropPath = BACKDROPPATH;
        userLists1.get (1).backDropPath = BACKDROPPATH;
        userLists1.get (2).backDropPath = BACKDROPPATH;

        given (database.getUserLists ()).willReturn (Flowable.just (userLists));

        given (database.isListEmpty (any ())).willReturn (false);

        given (database.getTopMovieFromList (any ()))
                .will (invocation -> {
                    Movie movie = new Movie ();
                    movie.backdropPath = BACKDROPPATH;
                    return movie;
                });

        presenter.attachView (view, navigator);

        Thread.sleep (1000);

        verify (database).anyUserListExists ();
        verify (view).getDefaultLists ();
        verify (database).createDefaultUserLists (defaultLists);
        verify (database).getUserLists ();
        verify (database, times (3)).getTopMovieFromList (any ());
        verify (view).showCharts (userLists1, 0);
    }

    @Test
    public void saveState_shouldHaveCorrectIndex () throws InterruptedException {

        given (database.anyUserListExists ())
                .willReturn (Single.just (false));

        List<String> defaultLists = new ArrayList<> ();
        defaultLists.add ("Watched");
        defaultLists.add ("Watching");
        defaultLists.add ("Collection");

        given (view.getDefaultLists ()).willReturn (defaultLists);

        List<UserList> userLists = new ArrayList<> ();
        userLists.add (new UserList (defaultLists.get (0)));
        userLists.add (new UserList (defaultLists.get (1)));
        userLists.add (new UserList (defaultLists.get (2)));

        given (database.getUserLists ()).willReturn (Flowable.just (userLists));

        given (database.isListEmpty (any ())).willReturn (false);

        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());

        presenter.attachView (view, navigator);

        Thread.sleep (1000);

        verify (view).showCharts (userLists, 0);

        presenter.saveState (3);
        presenter.detachView ();

        presenter.attachView (view, navigator);

        Thread.sleep (1000);

        verify (view).showCharts (userLists, 3);
    }


    @Test
    public void onListClick_shouldCallNavigator () {

        given (database.anyUserListExists ())
                .willReturn (Single.just (true));

        List<UserList> userLists = new ArrayList<> ();
        given (database.getUserLists ()).willReturn (Flowable.just (userLists));
        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());

        UserList userList = new UserList ();

        presenter.attachView (view, navigator);

        presenter.onUserListClicked (userList);

        verify (navigator).openMyList (userList);
    }

    @Test
    public void onAddClick_shouldCallNavigator () {

        given (database.anyUserListExists ())
                .willReturn (Single.just (true));

        List<UserList> userLists = new ArrayList<> ();
        given (database.getUserLists ()).willReturn (Flowable.just (userLists));
        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());

        presenter.attachView (view, navigator);

        presenter.onAddClicked ();

        verify (navigator).openCreateList (Movie.NONE);
    }

    @Test
    public void onRenameClick_shouldCallNavigator () {

        given (database.anyUserListExists ())
                .willReturn (Single.just (true));

        List<UserList> userLists = new ArrayList<> ();
        given (database.getUserLists ()).willReturn (Flowable.just (userLists));
        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());

        UserList userList = new UserList ();

        presenter.attachView (view, navigator);

        presenter.onRenameClicked (userList);

        verify (navigator).openRenameList (userList);
    }

    @Test
    public void onDeleteClick_shouldCallNavigator () {

        given (database.anyUserListExists ())
                .willReturn (Single.just (true));

        List<UserList> userLists = new ArrayList<> ();
        given (database.getUserLists ()).willReturn (Flowable.just (userLists));
        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());
        given (database.deleteList (any ()))
                .willReturn (Completable.complete ());
        UserList userList = new UserList ();

        presenter.attachView (view, navigator);

        presenter.deleteList (userList);

        verify (database).deleteList (userList);
    }

    @Test
    public void onDuplicateClick_shouldCallNavigator () {

        given (database.anyUserListExists ())
                .willReturn (Single.just (true));

        List<UserList> userLists = new ArrayList<> ();
        given (database.getUserLists ()).willReturn (Flowable.just (userLists));
        given (database.getTopMovieFromList (any ()))
                .will (invocation -> new Movie ());
        given (database.duplicateListItems (any ()))
                .willReturn (Completable.complete ());
        UserList userList = new UserList ();

        presenter.attachView (view, navigator);

        presenter.duplicateList (userList);

        verify (database).duplicateListItems (userList);
    }
}
