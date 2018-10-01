package com.winterparadox.themovieapp.room;

import android.content.Context;

import com.winterparadox.themovieapp.common.beans.Favorite;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.FavoriteDao;
import com.winterparadox.themovieapp.common.room.MovieDao;

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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FavoriteDaoTest {

    private FavoriteDao favoriteDao;
    private MovieDao movieDao;
    private AppDatabase mDb;

    @Before
    public void createDb () {
        Context context = InstrumentationRegistry.getTargetContext ();
        mDb = Room.inMemoryDatabaseBuilder (context, AppDatabase.class).build ();
        favoriteDao = mDb.favoriteDao ();
        movieDao = mDb.movieDao ();
    }

    @After
    public void closeDb () {
        mDb.close ();
    }

    @Test
    public void writeFavsAndRead () {
        insertMovies ();
        Favorite favorite = new Favorite ();
        favorite.movieId = 800L;
        favorite.time = System.currentTimeMillis ();

        favoriteDao.insertAll (favorite);

        List<Movie> favsById = favoriteDao.getFavorites ().blockingGet ();
        assertThat (favsById.size (), equalTo (1));
        assertThat (favsById.get (0).id, equalTo (favorite.movieId));
    }

    @Test
    public void loadFavs () {
        insertMovies ();

        for ( int i = 0; i < 10; i++ ) {
            Favorite favorite = new Favorite ();
            favorite.movieId = 100 + 100 * i;
            favorite.time = System.currentTimeMillis ();
            favoriteDao.insertAll (favorite);
        }


        List<Movie> favorites = favoriteDao.getFavorites ().blockingGet ();
        assertThat (favorites.size (), equalTo (10));

        List<Movie> homeFavorites = favoriteDao.getHomeFavorites (5).blockingGet ();
        assertThat (homeFavorites.size (), equalTo (5));
    }

    @Test
    public void deleteFavs () {
        insertMovies ();

        for ( int i = 0; i < 10; i++ ) {
            Favorite favorite = new Favorite ();
            favorite.movieId = 100 + 100 * i;
            favorite.time = System.currentTimeMillis ();
            favoriteDao.insertAll (favorite);
        }


        favoriteDao.deleteAll (new Favorite (500L));
        List<Movie> favorites = favoriteDao.getFavorites ().blockingGet ();
        assertThat (favorites.size (), equalTo (9));
        assertThat (favorites, not (hasItem (new Movie (500L))));
    }

    @Test
    public void isFavorite () {
        insertMovies ();

        for ( int i = 0; i < 10; i++ ) {
            Favorite favorite = new Favorite ();
            favorite.movieId = 100 + 100 * i;
            favorite.time = System.currentTimeMillis ();
            favoriteDao.insertAll (favorite);
        }

        Boolean isFavBefore = favoriteDao.isFavorite (500L).blockingGet ();
        assertThat (isFavBefore, equalTo (true));

        favoriteDao.deleteAll (new Favorite (500L));

        Boolean isFavAfter = favoriteDao.isFavorite (500L).blockingGet ();
        assertThat (isFavAfter, equalTo (false));
    }

    @Test
    public void anyExists () {
        insertMovies ();

        Boolean anyBefore = favoriteDao.anyExists ().blockingFirst ();
        assertThat (anyBefore, equalTo (false));

        for ( int i = 0; i < 10; i++ ) {
            Favorite favorite = new Favorite ();
            favorite.movieId = 100 + 100 * i;
            favorite.time = System.currentTimeMillis ();
            favoriteDao.insertAll (favorite);
        }

        Boolean anyAfter = favoriteDao.anyExists ().blockingFirst ();
        assertThat (anyAfter, equalTo (true));
    }

    private void insertMovies () {
        for ( int i = 0; i < 10; i++ ) {
            Movie movie = new Movie (100 + 100 * i);
            movieDao.insertAll (movie);
        }
    }
}
