package com.winterparadox.themovieapp.room;

import android.content.Context;

import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.ChartDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ChartDaoTest {

    private ChartDao chartDao;
    private AppDatabase mDb;

    @Before
    public void createDb () {
        Context context = InstrumentationRegistry.getTargetContext ();
        mDb = Room.inMemoryDatabaseBuilder (context, AppDatabase.class).build ();
        chartDao = mDb.chartDao ();
    }

    @After
    public void closeDb () {
        mDb.close ();
    }

    @Test
    public void writeChartAndRead () {
        Chart chart = new Chart ();
        chart.id = 800L;
        chart.name = "Top Rated";
        chart.backDropPath = "the_image_path";

        chartDao.insert (chart);

        Chart chartById = chartDao.getChart (chart.id).blockingGet ();
        assertThat (chartById, equalTo (chart));
        assertThat (chartById.name, equalTo (chart.name));
        assertThat (chartById.backDropPath, equalTo (chart.backDropPath));
    }

    @Test
    public void loadCharts () {
        for ( int i = 0; i < 10; i++ ) {
            Chart chart = new Chart ();
            chart.id = 800L + i;
            chart.name = "Top Rated" + i;
            chart.backDropPath = "the_image_path" + i;
            chartDao.insert (chart);
        }


        List<Chart> charts = chartDao.getCharts ().blockingFirst ();
        assertThat (charts.size (), equalTo (10));
    }

    @Test
    public void updateChart () {
        Chart chart = new Chart ();
        chart.id = 800L;
        chart.name = "Top Rated";
        chart.backDropPath = "the_image_path";

        chartDao.insert (chart);

        Chart chartById = chartDao.getChart (chart.id).blockingGet ();
        assertThat (chartById, equalTo (chart));

        chartById.name = "New Name";
        chartDao.update (chartById);

        Chart chartUpdated = chartDao.getChart (chart.id).blockingGet ();
        assertThat (chartUpdated.name, equalTo (chartById.name));
    }

}
