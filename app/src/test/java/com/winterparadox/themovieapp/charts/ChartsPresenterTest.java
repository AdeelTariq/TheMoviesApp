package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Chart;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChartsPresenterTest {

    private ChartsPresenter presenter;

    @Mock
    Navigator navigator;

    @Mock
    ChartsView view;

    @Mock
    ChartsApiInteractor api;

    @Mock
    ChartsDatabaseInteractor database;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        Scheduler scheduler = Schedulers.trampoline ();
        presenter = new ChartsPresenterImpl (api,
                database, scheduler);
    }

    @Test
    public void attachView_loadChartsAndShow () {

        List<Chart> charts = new ArrayList<> ();
        charts.add (new Chart ());
        charts.add (new Chart ());

        given (database.getCharts ()).willReturn (Flowable.just (charts));

        presenter.attachView (view, navigator);

        then (database).should ().getCharts ();
        then (view).should ().showCharts (charts, 0);
    }

    @Test
    public void attachView_loadChartsError () {

        String errorMessage = "Error";

        given (database.getCharts ())
                .willReturn (Flowable.error (new Throwable (errorMessage)));

        presenter.attachView (view, navigator);

        then (database).should ().getCharts ();
        then (view).should ().showError (errorMessage);

    }

    @Test
    public void saveState_shouldShowCorrectIndex () {

        List<Chart> charts = new ArrayList<> ();
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());
        charts.add (new Chart ());

        given (database.getCharts ()).willReturn (Flowable.just (charts));

        presenter.attachView (view, navigator);

        presenter.saveState (4);
        presenter.detachView ();

        presenter.attachView (view, navigator);

        then (database).should (times (2)).getCharts ();
        then (view).should ().showCharts (charts, 0);
        then (view).should ().showCharts (charts, 4);
    }

    @Test
    public void chartClicked_shouldCallNavigator () {

        Chart chart = new Chart (900L, "chart");
        given (database.getCharts ())
                .willReturn (Flowable.just (new ArrayList<> ()));

        presenter.attachView (view, navigator);

        presenter.onChartClicked (chart);

        then (navigator).should ().openChartMovieList (chart);
    }

    @Test
    public void fetchCharts_fetchAndStoreCharts () throws InterruptedException {

        given (database.getCharts ()).willReturn (Flowable.just (new ArrayList<> ()));

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

        given (database.update (any (Chart.class)))
                .willReturn (0L);
        given (database.insert (any (Chart.class)))
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
        verify (database, times (7)).insert (any ());
        verify (database, times (7)).getChart (anyLong ());
        then (api).should (times (1))
                .popularMovieBackdrop (popular);
        then (api).should (times (1))
                .latestMovieBackdrop (latest);
        then (api).should (times (1))
                .topRatedMovieBackdrop (top_rated);
        then (api).should (times (4))
                .genreMovieBackdrop (any ());
        then (database).should (times (7)).update (any ());
    }
}
