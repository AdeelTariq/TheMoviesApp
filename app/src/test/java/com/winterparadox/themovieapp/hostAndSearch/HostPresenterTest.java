package com.winterparadox.themovieapp.hostAndSearch;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HostPresenterTest {

    private HostPresenter presenter;

    @Mock
    HostView view;

    @Mock
    Navigator navigator;

    @Mock
    HostDatabaseInteractor database;
    @Mock
    HostApiInteractor api;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler mainScheduler = Schedulers.trampoline ();
        presenter = new HostPresenterImpl (api, database, mainScheduler);
    }

    @Test
    public void attachView_shouldFetchChartDataAndCheckFavsAndRecents () {

        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));


        presenter.attachView (view, navigator);

        verify (database).anyFavoriteExists ();
        verify (database).anyRecentyViewedExists ();
        verify (view).showFavoritesMenu (true);
        verify (view).showRecentMenu (false);
    }

    @Test
    public void fetchChartData_shouldFetchFromApiAndSaveInDatabase () throws InterruptedException {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        List<Chart> charts = new ArrayList<> ();
        charts.add (new Chart (4, "four"));
        charts.add (new Chart (5, "five"));
        charts.add (new Chart (6, "six"));
        charts.add (new Chart (7, "seven"));

        String backdrop1 = "image_path_1";

        Chart popular = new Chart (0, "Popular");
        Chart popularBD = new Chart (0, "Popular");
        popularBD.backDropPath = backdrop1;
        Chart latest = new Chart (1, "Latest");
        Chart latestBD = new Chart (1, "Latest");
        latestBD.backDropPath = backdrop1;
        Chart top_rated = new Chart (2, "Top Rated");
        Chart top_ratedBD = new Chart (2, "Top Rated");
        top_ratedBD.backDropPath = backdrop1;

        given (database.getChart (anyLong ()))
                .will (invocation -> {
                    long argument = invocation.getArgument (0);
                    switch ( (int) argument ) {
                        case 0:
                            return Single.just (popular);
                        case 1:
                            return Single.just (latest);
                        case 2:
                            return Single.just (top_rated);
                        default:
                            return Single.just (charts.get ((int) (argument - 4)));
                    }
                });

        given (database.updateChart (any (Chart.class)))
                .willReturn (0L);
        given (database.insertChart (any (Chart.class)))
                .willReturn (0L);

        given (api.genres ()).willReturn (Single.just (charts));
        given (api.genreMovieBackdrop (any (Chart.class)))
                .will ((Answer<Single<Chart>>) invocation -> {
                    Chart argument = invocation.getArgument (0);
                    argument.backDropPath = backdrop1;
                    return Single.just (argument);
                });

        given (api.popularMovieBackdrop (any (Chart.class)))
                .willReturn (Single.just (popularBD));
        given (api.latestMovieBackdrop (any (Chart.class)))
                .willReturn (Single.just (latestBD));
        given (api.topRatedMovieBackdrop (any (Chart.class)))
                .willReturn (Single.just (top_ratedBD));

        ArrayList<String> strings = new ArrayList<> ();
        strings.add ("Popular");
        strings.add ("Latest");
        strings.add ("Top Rated");
        given (view.getDefaultCharts ()).willReturn (strings);

        presenter.attachView (view, navigator);
        presenter.fetchChartData ();

        Thread.sleep (1000);

        verify (api).genres ();
        then (view).should ().getDefaultCharts ();
        verify (database, times (7)).insertChart (any ());
        verify (database, times (7)).getChart (anyLong ());
        then (api).should (times (1))
                .popularMovieBackdrop (popular);
        then (api).should (times (1))
                .latestMovieBackdrop (latest);
        then (api).should (times (1))
                .topRatedMovieBackdrop (top_rated);
        then (api).should (times (4))
                .genreMovieBackdrop (any ());
        then (database).should (times (7)).updateChart (any ());
    }

    @Test
    public void search_shouldSearchDatabase () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie ("2018-01-05"));
        movies.add (new Movie ("2018-03-04"));
        movies.add (new Movie ("2018-05-03"));
        movies.add (new Movie ("2018-06-02"));

        given (api.search (anyString ())).willReturn (Single.just (movies));

        String query = "movie";

        presenter.attachView (view, navigator);
        presenter.search (query);

        verify (view).showProgress ();
        verify (api).search (query);
        verify (view).hideProgress ();
        verify (navigator).openSearchResults (query, movies);
    }

    @Test
    public void getSuggestions_shouldSearchDatabase () throws InterruptedException {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        String movie = "movie";
        List<Movie> movies = new ArrayList<> ();
        movies.add (new Movie (100, "top Movie"));
        movies.add (new Movie (200, "Hi Movie action"));
        movies.add (new Movie (300, "Movie bad"));
        movies.add (new Movie (400, "Movie hit"));

        given (database.getSuggestions (anyString ())).willReturn (Single.just (movies));

        List<Movie> moviesSorted = new ArrayList<> ();
        moviesSorted.add (new Movie (300, "Movie bad"));
        moviesSorted.add (new Movie (400, "Movie hit"));
        moviesSorted.add (new Movie (200, "Hi Movie action"));
        moviesSorted.add (new Movie (100, "top Movie"));

        presenter.attachView (view, navigator);
        presenter.getSuggestions (movie);

        Thread.sleep (1000);

        verify (database).getSuggestions (movie);
        verify (view).showSuggestions (moviesSorted);

        presenter.getSuggestions ("mo");

        Thread.sleep (1000);

        verify (view).clearSuggestions ();
    }

    @Test
    public void favoriteClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        presenter.onFavoritesClicked ();

        verify (navigator).openFavorites ();
    }

    @Test
    public void recentlyViewedClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        presenter.onRecentlyViewedClicked ();

        verify (navigator).openRecentlyViewed ();
    }

    @Test
    public void myListsClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        presenter.onMyListsClicked ();

        verify (navigator).openMyLists ();
    }

    @Test
    public void chartsClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        presenter.onChartsClicked ();

        verify (navigator).openCharts ();
    }

    @Test
    public void aboutClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        presenter.onAboutClicked ();

        verify (navigator).openAbout ();
    }

    @Test
    public void movieSuggestionClick_shouldCallNavigator () {
        given (database.anyFavoriteExists ()).willReturn (Flowable.just (true));
        given (database.anyRecentyViewedExists ()).willReturn (Flowable.just (false));

        presenter.attachView (view, navigator);
        Movie movie = new Movie ();
        presenter.onMovieSuggestionClicked (movie);

        verify (navigator).openMovie (movie, null);
    }

}
