package com.winterparadox.themovieapp.movieDetails;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Credits;
import com.winterparadox.themovieapp.common.beans.GenresItem;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.common.beans.RegionItem;
import com.winterparadox.themovieapp.common.beans.ReleaseDates;
import com.winterparadox.themovieapp.common.beans.ReleaseDatesItem;
import com.winterparadox.themovieapp.common.beans.UserList;

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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MovieDetailsPresenterTest {

    private MovieDetailsPresenter presenter;

    @Mock
    MovieDetailsView view;

    @Mock
    Navigator navigator;

    @Mock
    MovieDetailsApiInteractor api;

    @Mock
    MovieDetailsDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new MovieDetailsPresenterImpl (api, mainScheduler, database);
    }

    @Test
    public void attachView_shouldCallDatabase () {

        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";

        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));

        presenter.attachView (view, movie, navigator);

        verify (database).addToCache (movie);
        verify (database).addToRecentlyViewed (movie);
        verify (database).isFavorite (movie);
        verify (view).showFavorite (true);
        verify (view).showMovie (movie, "(2019)");

    }


    @Test
    public void isFavoriteAndSetFav_shouldReturnCorrectValue () {

        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";

        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));
        given (database.addToFavorite (any ())).willReturn (Completable.complete ());
        given (database.unFavorite (any ())).willReturn (Completable.complete ());

        presenter.attachView (view, movie, navigator);

        verify (database).addToCache (movie);
        verify (database).addToRecentlyViewed (movie);
        verify (database).isFavorite (movie);
        verify (view).showFavorite (true);
        verify (view).showMovie (movie, "(2019)");
        assertTrue ("Incorrect fav value", presenter.isMovieFav ());

        presenter.setMovieFav (false);
        verify (database).unFavorite (movie);
        assertFalse ("Incorrect fav value", presenter.isMovieFav ());

        presenter.setMovieFav (true);
        verify (database).addToFavorite (movie);
        assertTrue ("Incorrect fav value", presenter.isMovieFav ());
    }

    @Test
    public void fetchAdditionalDetails_shouldFetchMovieDetailsAndShow () {

        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";

        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));

        Movie item = new Movie (100L, "movie");
        item.credits = new Credits ();
        item.credits.cast = new ArrayList<> ();
        item.credits.crew = new ArrayList<> ();
        item.releaseDates = new ReleaseDates ();
        item.releaseDates.results = new ArrayList<> ();

        RegionItem regionItem = new RegionItem ();
        regionItem.iso31661 = "US";
        regionItem.releaseDates = new ArrayList<> ();
        ReleaseDatesItem releaseDatesItem = new ReleaseDatesItem ();
        releaseDatesItem.certification = "R";
        regionItem.releaseDates.add (releaseDatesItem);

        RegionItem regionItem1 = new RegionItem ();
        regionItem1.iso31661 = "PK";
        regionItem1.releaseDates = new ArrayList<> ();
        ReleaseDatesItem releaseDatesItem1 = new ReleaseDatesItem ();
        releaseDatesItem1.certification = "PG";
        regionItem1.releaseDates.add (releaseDatesItem1);

        item.releaseDates.results.add (regionItem);
        item.releaseDates.results.add (regionItem1);
        item.genres = new ArrayList<> ();
        item.genres.add (new GenresItem ("Action"));
        item.runtime = 70;

        given (api.movieDetails (anyLong ()))
                .willReturn (Single.just (item));

        presenter.attachView (view, movie, navigator);
        presenter.fetchAdditionalDetails ();

        verify (view).showProgress ();
        verify (api).movieDetails (movie.id);
        verify (view).showAdditionalDetails (item, "R", "1h 10m", "Action");
    }

    @Test
    public void addToListClick_shouldLoadUserLists () throws InterruptedException {
        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";

        List<String> list = new ArrayList<> ();
        list.add ("Watching");
        list.add ("Watched");

        ArrayList<UserList> userLists = new ArrayList<> ();
        userLists.add (new UserList (100L, list.get (0)));
        userLists.add (new UserList (200L, list.get (1)));

        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));
        given (database.anyUserListExists ()).willReturn (Single.just (false));
        given (view.getDefaultLists ()).willReturn (list);
        given (database.getUserLists ()).willReturn (Single.just (userLists));
        given (database.isMovieInList (anyLong (), anyLong ())).willReturn (false);

        presenter.attachView (view, movie, navigator);

        presenter.onAddToListClicked ();

        Thread.sleep (1000);

        verify (database).anyUserListExists ();
        verify (view).getDefaultLists ();
        verify (database).createDefaultUserLists (list);
        verify (database).getUserLists ();
        verify (database).isMovieInList (movie.id, 100L);
        verify (database).isMovieInList (movie.id, 200L);
        verify (navigator).openListSelector (userLists, movie.id);
    }

    @Test
    public void onMovieClick_shouldCallNavigator () {
        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";
        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));
        Object element = new Object ();

        presenter.attachView (view, movie, navigator);

        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }

    @Test
    public void onPersonClick_shouldCallNavigator () {
        Movie movie = new Movie (100L, "movie");
        movie.releaseDate = "2019-09-09";
        given (database.addToCache (any ())).willReturn (Completable.complete ());
        given (database.addToRecentlyViewed (any ())).willReturn (Completable.complete ());
        given (database.isFavorite (any ())).willReturn (Single.just (true));
        Object element = new Object ();

        presenter.attachView (view, movie, navigator);

        Person person = new Person ();
        presenter.onPersonClicked (person, element);

        verify (navigator).openPerson (person, element);
    }
}
