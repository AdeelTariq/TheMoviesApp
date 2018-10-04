package com.winterparadox.themovieapp.favorites;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class FavoritesPresenterTest {

    private FavoritesPresenter presenter;

    @Mock
    FavoritesView view;

    @Mock
    Navigator navigator;

    @Mock
    FavoritesDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new FavoritesPresenterImpl (database, mainScheduler);
    }

    @Test
    public void attachView_loadAndShowMovies () {

        ArrayList<Movie> movies = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.releaseDate = "2018-07-12";
        Movie movie1 = new Movie ();
        movie1.releaseDate = "2016-07-12";
        movies.add (movie);
        movies.add (movie1);
        given (database.getFavorites ()).willReturn (Single.just (movies));

        presenter.attachView (view, navigator);

        verify (view).showProgress ();
        verify (database).getFavorites ();
        verify (view).hideProgress ();
        verify (view, new Times (1) {
            @Override
            public void verify (VerificationData data) {
                super.verify (data);
                Invocation invocation = data.getAllInvocations ().get (1);
                List<Movie> argument = invocation.getArgument (0);
                assertTrue ("Wrong year", "2018".equals (argument.get (0).year));
                assertTrue ("Wrong year", "2016".equals (argument.get (1).year));
            }
        }).showMovies (movies, 0);
    }

    @Test
    public void attachView_loadAndShowError () {

        String errString = "Error";
        Throwable throwable = new Throwable (errString);
        given (database.getFavorites ()).willReturn (Single.error (throwable));

        presenter.attachView (view, navigator);

        verify (view).showProgress ();
        verify (database).getFavorites ();
        verify (view).hideProgress ();
        verify (view).showError (errString);
    }

    @Test
    public void saveState_shouldHaveCorrectIndex () {

        ArrayList<Movie> movies = new ArrayList<> ();
        Movie movie = new Movie ();
        movie.releaseDate = "2018-07-12";
        Movie movie1 = new Movie ();
        movie1.releaseDate = "2016-07-12";
        movies.add (movie);
        movies.add (movie1);
        given (database.getFavorites ()).willReturn (Single.just (movies));

        presenter.attachView (view, navigator);

        presenter.saveState (4);
        presenter.detachView ();

        presenter.attachView (view, navigator);

        verify (view).showMovies (movies, 0);
        verify (view).showMovies (movies, 4);
    }

    @Test
    public void unFavorite_shouldCallDatabase () {

        Movie movie = new Movie ();
        given (database.getFavorites ()).willReturn (Single.just (new ArrayList<> ()));
        given (database.unFavorite (any (Movie.class)))
                .willReturn (Completable.complete ());

        presenter.attachView (view, navigator);

        presenter.unFavorite (movie);

        verify (database).unFavorite (movie);
    }


    @Test
    public void movieClick_shouldCallNavigator () {

        Movie movie = new Movie ();
        Object element = new Object ();
        given (database.getFavorites ()).willReturn (Single.just (new ArrayList<> ()));

        presenter.attachView (view, navigator);

        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }

}
