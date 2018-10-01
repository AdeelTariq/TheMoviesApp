package com.winterparadox.themovieapp.room;

import android.content.Context;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.RecentlyViewed;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.RecentlyViewedDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class RecentlyViewedDaoTest {

    private RecentlyViewedDao recentlyViewedDao;
    private MovieDao movieDao;
    private AppDatabase mDb;

    @Before
    public void createDb () {
        Context context = InstrumentationRegistry.getTargetContext ();
        mDb = Room.inMemoryDatabaseBuilder (context, AppDatabase.class).build ();
        recentlyViewedDao = mDb.recentlyViewedDao ();
        movieDao = mDb.movieDao ();
    }

    @After
    public void closeDb () {
        mDb.close ();
    }

    @Test
    public void writeRecentsAndRead () {
        insertMovies ();
        RecentlyViewed RecentlyViewed = new RecentlyViewed ();
        RecentlyViewed.movieId = 800L;
        RecentlyViewed.time = System.currentTimeMillis ();

        recentlyViewedDao.insertAll (RecentlyViewed);

        List<Movie> favsById = recentlyViewedDao.getRecent (1).blockingGet ();
        assertThat (favsById.size (), equalTo (1));
        assertThat (favsById.get (0).id, equalTo (RecentlyViewed.movieId));
    }

    @Test
    public void loadRecents () {
        insertMovies ();

        for ( int i = 0; i < 10; i++ ) {
            RecentlyViewed RecentlyViewed = new RecentlyViewed ();
            RecentlyViewed.movieId = 100 + 100 * i;
            RecentlyViewed.time = System.currentTimeMillis ();
            recentlyViewedDao.insertAll (RecentlyViewed);
        }


        List<Movie> RecentlyVieweds = recentlyViewedDao.getRecent (99).blockingGet ();
        assertThat (RecentlyVieweds.size (), equalTo (10));

        List<Movie> homeRecentlyVieweds = recentlyViewedDao.getRecent (5).blockingGet ();
        assertThat (homeRecentlyVieweds.size (), equalTo (5));
    }

    @Test
    public void anyExists () {
        insertMovies ();

        Boolean anyBefore = recentlyViewedDao.anyExists ().blockingFirst ();
        assertThat (anyBefore, equalTo (false));

        for ( int i = 0; i < 10; i++ ) {
            RecentlyViewed RecentlyViewed = new RecentlyViewed ();
            RecentlyViewed.movieId = 100 + 100 * i;
            RecentlyViewed.time = System.currentTimeMillis ();
            recentlyViewedDao.insertAll (RecentlyViewed);
        }

        Boolean anyAfter = recentlyViewedDao.anyExists ().blockingFirst ();
        assertThat (anyAfter, equalTo (true));
    }

    private void insertMovies () {
        for ( int i = 0; i < 10; i++ ) {
            Movie movie = new Movie (100 + 100 * i);
            movieDao.insertAll (movie);
        }
    }
}
