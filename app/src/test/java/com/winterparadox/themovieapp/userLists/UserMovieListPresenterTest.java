package com.winterparadox.themovieapp.userLists;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListDatabaseInteractor;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListPresenter;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListPresenterImpl;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class UserMovieListPresenterTest {

    private UserMovieListPresenter presenter;

    @Mock
    UserMovieListView view;

    @Mock
    Navigator navigator;

    @Mock
    UserMovieListDatabaseInteractor database;

    private UserList userList;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler scheduler = Schedulers.trampoline ();
        userList = new UserList (100L, "Watched");
        presenter = new UserMovieListPresenterImpl (database, scheduler);
    }

    @Test
    public void attachView_shouldCallDatabase () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("1919-09-09"));
        movies.add (new Movie ("2019-09-09"));

        given (database.getListMovies (any ())).willReturn (Single.just (movies));

        presenter.attachView (view, userList, navigator);

        verify (database).getListMovies (userList);
        verify (view).showMovies (movies);
    }

    @Test
    public void onMovieClick_shouldCallNavigator () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("1919-09-09"));
        movies.add (new Movie ("2019-09-09"));

        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";

        given (database.getListMovies (any ())).willReturn (Single.just (movies));

        Object element = new Object ();

        presenter.attachView (view, navigator);

        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }

    @Test
    public void onDiscoverClick_shouldCallNavigator () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("1919-09-09"));
        movies.add (new Movie ("2019-09-09"));

        given (database.getListMovies (any ())).willReturn (Single.just (movies));

        presenter.attachView (view, navigator);

        presenter.onDiscoverClick ();

        verify (navigator).openCharts ();
    }

    @Test
    public void deleteMovie_shouldCallNavigator () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("1919-09-09"));
        movies.add (new Movie ("2019-09-09"));

        given (database.getListMovies (any ())).willReturn (Single.just (movies));
        given (database.deleteFromList (any (), any ()))
                .willReturn (Completable.complete ());

        presenter.attachView (view, userList, navigator);

        presenter.deleteMovie (movies.get (0));

        verify (database).deleteFromList (userList, movies.get (0));
    }

    @Test
    public void saveListOrder_shouldCallNavigator () {
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("1919-09-09"));
        movies.add (new Movie ("2019-09-09"));

        given (database.getListMovies (any ())).willReturn (Single.just (movies));
        given (database.saveListOrder (any (), any ()))
                .willReturn (Completable.complete ());

        presenter.attachView (view, userList, navigator);

        ArrayList<Object> movies1 = new ArrayList<> (movies);

        presenter.saveListOrder (movies1);

        verify (database).saveListOrder (userList, movies);
    }

}
