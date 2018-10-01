package com.winterparadox.themovieapp.room;

import android.content.Context;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.MovieDao;

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
public class MovieDaoTest {

    private MovieDao movieDao;
    private AppDatabase mDb;

    @Before
    public void createDb () {
        Context context = InstrumentationRegistry.getTargetContext ();
        mDb = Room.inMemoryDatabaseBuilder (context, AppDatabase.class).build ();
        movieDao = mDb.movieDao ();
    }

    @After
    public void closeDb () {
        mDb.close ();
    }

    @Test
    public void writeMovieAndRead () {
        Movie movie = new Movie ();
        movie.id = 800L;
        movie.title = "Top Gear";
        movie.backdropPath = "the_image_path";

        movieDao.insertAll (movie);

        List<Movie> movies = movieDao.getAll ().blockingGet ();
        assertThat (movies.size (), equalTo (1));
        assertThat (movies.get (0).title, equalTo (movie.title));
        assertThat (movies.get (0).backdropPath, equalTo (movie.backdropPath));
    }

    @Test
    public void loadMovies () {
        for ( int i = 0; i < 10; i++ ) {
            Movie movie = new Movie ();
            movie.id = 800L + i;
            movie.title = "Top Rated" + i;
            movieDao.insertAll (movie);
        }


        List<Movie> movies = movieDao.getAll ().blockingGet ();
        assertThat (movies.size (), equalTo (10));
    }

    @Test
    public void searchMovies () {
        String name = "Top Rated";
        for ( int i = 0; i < 10; i++ ) {
            Movie movie = new Movie ();
            movie.id = 800L + i;
            movie.title = name + i;
            movieDao.insertAll (movie);
        }

        List<Movie> movies = movieDao.search ("%" + name + "%", 5).blockingGet ();
        assertThat (movies.size (), equalTo (5));

        List<Movie> movies1 = movieDao.search ("%" + name + "%", 99).blockingGet ();
        assertThat (movies1.size (), equalTo (10));

        List<Movie> movies2 = movieDao.search ("%" + name + 1 + "%", 99).blockingGet ();
        assertThat (movies2.size (), equalTo (1));
    }

}
