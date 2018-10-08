package com.winterparadox.themovieapp.home;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.HomeSection;
import com.winterparadox.themovieapp.common.beans.Movie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HomePresenterTest {

    private HomePresenter presenter;

    @Mock
    HomeView view;

    @Mock
    Navigator navigator;

    @Mock
    HomeDatabaseInteractor database;
    @Mock
    HomeApiInteractor api;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new HomePresenterImpl (api, database, mainScheduler);
    }

    @Test
    public void fetchData_loadDataAndShow () throws InterruptedException {
        String fav = "Fav";
        given (view.favoriteTitle ()).willReturn (fav);
        String recent = "Recent";
        given (view.recentlyTitle ()).willReturn (recent);
        String popular = "Popular";
        given (view.popularTitle ()).willReturn (popular);
        String upcoming = "Upcoming";
        given (view.upcomingTitle ()).willReturn (upcoming);

        ArrayList<Movie> movies1 = getMovies (6);
        ArrayList<Movie> movies2 = getMovies (8);
        ArrayList<Movie> movies3 = getMovies (5);
        ArrayList<Movie> movies4 = getMovies (9);

        given (database.getHomeFavorites ()).willReturn (Single.just (movies1));
        given (database.getHomeRecents ()).willReturn (Single.just (movies2));
        given (api.popularMovies ()).willReturn (Single.just (movies3));
        given (api.upcomingMovies ()).willReturn (Single.just (movies4));

        ArrayList<Object> section1 = getSectionObjects (movies1,
                HomeSection.SECTION_FAVORITES, fav);
        ArrayList<Object> section2 = getSectionObjects (movies2,
                HomeSection.SECTION_RECENT, recent);
        ArrayList<Object> section3 = getSectionObjects (movies3,
                HomeSection.SECTION_POPULAR, popular);
        ArrayList<Object> section4 = getSectionObjects (movies4,
                HomeSection.SECTION_UPCOMING, upcoming);

        presenter.attachView (view, navigator);
        presenter.fetchData ();

        Thread.sleep (1000);

        verify (view).showProgress ();
        verify (view).clearView ();
        verify (database).getHomeRecents ();
        verify (database).getHomeFavorites ();
        verify (api).popularMovies ();
        verify (api).upcomingMovies ();
        verify (view).showMovies (section1);
        verify (view).showMovies (section2);
        verify (view).showMovies (section3);
        verify (view).showMovies (section4);
        verify (view, times (4)).hideProgress ();

    }

    @Test
    public void fetchData_shouldCheckSize () throws InterruptedException {
        String fav = "Fav";
        given (view.favoriteTitle ()).willReturn (fav);
        String recent = "Recent";
        given (view.recentlyTitle ()).willReturn (recent);
        String popular = "Popular";
        given (view.popularTitle ()).willReturn (popular);
        String upcoming = "Upcoming";
        given (view.upcomingTitle ()).willReturn (upcoming);

        ArrayList<Movie> movies1 = getMovies (2);
        ArrayList<Movie> movies2 = getMovies (8);
        ArrayList<Movie> movies3 = getMovies (3);
        ArrayList<Movie> movies4 = getMovies (9);

        given (database.getHomeFavorites ()).willReturn (Single.just (movies1));
        given (database.getHomeRecents ()).willReturn (Single.just (movies2));
        given (api.popularMovies ()).willReturn (Single.just (movies3));
        given (api.upcomingMovies ()).willReturn (Single.just (movies4));

        ArrayList<Object> section2 = getSectionObjects (movies2,
                HomeSection.SECTION_RECENT, recent);
        ArrayList<Object> section4 = getSectionObjects (movies4,
                HomeSection.SECTION_UPCOMING, upcoming);

        presenter.attachView (view, navigator);
        presenter.fetchData ();

        Thread.sleep (1000);

        verify (view).showProgress ();
        verify (view).clearView ();
        verify (database).getHomeRecents ();
        verify (database).getHomeFavorites ();
        verify (api).popularMovies ();
        verify (api).upcomingMovies ();
        verify (view).showMovies (section2);
        verify (view).showMovies (section4);
        verify (view, times (2)).hideProgress ();

    }


    private ArrayList<Object> getSectionObjects (ArrayList<Movie> movies,
                                                 int section, String title) {
        ArrayList<Object> objects = new ArrayList<> ();
        objects.add (new HomeSection (section, title));
        List<Movie> subList = movies;
        if ( movies.size () > 4 ) {
            subList = movies.subList (0, 4);
            Collections.sort (subList, (t, t1) -> Double.compare (t.voteAverage,
                    t1.voteAverage));
        }
        objects.addAll (subList);
        return objects;
    }


    private ArrayList<Movie> getMovies (int count) {
        ArrayList<Movie> movies = new ArrayList<> ();
        for ( int i = 0; i < count; i++ ) {
            movies.add (new Movie (100 + 100 * i));
        }
        return movies;
    }

    @Test
    public void movieClick_shouldCallNavigator () {
        Movie movie = new Movie ();
        Object element = new Object ();

        presenter.attachView (view, navigator);
        presenter.onMovieClicked (movie, element);

        verify (navigator).openMovie (movie, element);
    }

    @Test
    public void chartClick_shouldCallNavigator () {
        Chart chart = new Chart ();

        presenter.attachView (view, navigator);
        presenter.onChartClicked (chart);

        verify (navigator).openChartMovieList (chart);
    }

    @Test
    public void favoiteClick_shouldCallNavigator () {

        presenter.attachView (view, navigator);
        presenter.onFavoritesClicked ();

        verify (navigator).openFavorites ();
    }

    @Test
    public void recentlyViewedClick_shouldCallNavigator () {

        presenter.attachView (view, navigator);
        presenter.onRecentlyViewedClicked ();

        verify (navigator).openRecentlyViewed ();
    }

}
