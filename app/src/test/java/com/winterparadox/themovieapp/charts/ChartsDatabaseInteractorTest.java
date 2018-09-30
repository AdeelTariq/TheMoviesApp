package com.winterparadox.themovieapp.charts;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.room.ChartDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static io.reactivex.observers.BaseTestConsumer.TestWaitStrategy.SLEEP_1MS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class ChartsDatabaseInteractorTest {

    @Mock
    ChartDao chartDao;

    ChartsDatabaseInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new ChartsDatabaseInteractorImpl (chartDao);
    }

    @Test
    public void insert_shouldReturnFromChartsDao () {
        long value = 9000L;
        given (chartDao.insert (any (Chart.class)))
                .willReturn (value);

        Chart chart = new Chart ();

        long returnedId = interactor.insert (chart);

        then (chartDao).should ().insert (chart);
        assertEquals ("Wrong id returned", value, returnedId);
    }

    @Test
    public void update_shouldReturnFromChartsDao () {
        long value = 9000L;
        given (chartDao.update (any (Chart.class)))
                .willReturn (value);

        Chart chart = new Chart ();

        long returnedId = interactor.update (chart);

        then (chartDao).should ().update (chart);
        assertEquals ("Wrong id returned", value, returnedId);
    }

    @Test
    public void getChart_shouldReturnFromChartsDao () {
        Chart item = new Chart ();
        TestObserver<Chart> subscriber = new TestObserver<> ();
        given (chartDao.getChart (anyLong ())).willReturn (Single.just (item));

        long id = 9000L;

        interactor.getChart (id).subscribe (subscriber);

        then (chartDao).should ().getChart (id);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (item);
    }

    @Test
    public void getCharts_shouldReturnFromChartsDao () {
        Chart item = new Chart ();
        List<Chart> arrayList = new ArrayList<> ();
        arrayList.add (item);

        TestSubscriber<List<Chart>> subscriber = new TestSubscriber<> ();
        given (chartDao.getCharts ()).willReturn (Flowable.just (arrayList));

        interactor.getCharts ().subscribe (subscriber);

        then (chartDao).should ().getCharts ();
        subscriber.awaitCount (1, SLEEP_1MS, 5000);
        subscriber.assertValue (arrayList);
    }
}
