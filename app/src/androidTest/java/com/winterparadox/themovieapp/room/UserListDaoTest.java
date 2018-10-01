package com.winterparadox.themovieapp.room;

import android.content.Context;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;
import com.winterparadox.themovieapp.common.room.AppDatabase;
import com.winterparadox.themovieapp.common.room.MovieDao;
import com.winterparadox.themovieapp.common.room.UserListDao;

import org.hamcrest.CoreMatchers;
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
public class UserListDaoTest {

    private UserListDao userListDao;
    private MovieDao movieDao;
    private AppDatabase mDb;

    @Before
    public void createDb () {
        Context context = InstrumentationRegistry.getTargetContext ();
        mDb = Room.inMemoryDatabaseBuilder (context, AppDatabase.class).build ();
        userListDao = mDb.userListDao ();
        movieDao = mDb.movieDao ();
    }

    @After
    public void closeDb () {
        mDb.close ();
    }

    @Test
    public void writeUserListAndRead () {
        insertMovies ();
        UserList userList = new UserList ();
        userList.id = 800L;
        userList.name = "Watched";

        userListDao.insertList (userList);

        List<UserList> lists = userListDao.getUserListsOnce ().blockingGet ();
        assertThat (lists.size (), equalTo (1));
        assertThat (lists.get (0).id, equalTo (userList.id));
    }

    @Test
    public void loadUserLists () {
        insertMovies ();

        UserList[] userLists = new UserList[10];
        for ( int i = 0; i < 10; i++ ) {
            UserList userList = new UserList ();
            userList.id = 800L + i;
            userList.name = "Watched";
            userLists[i] = userList;
        }
        userListDao.insertAllLists (userLists);


        List<UserList> lists = userListDao.getUserLists ().blockingFirst ();
        assertThat (lists.size (), equalTo (10));
    }

    @Test
    public void updateUserLists () {
        insertMovies ();

        UserList userList = new UserList ();
        userList.id = 800L;
        userList.name = "Watched";

        userListDao.insertList (userList);

        List<UserList> lists = userListDao.getUserLists ().blockingFirst ();
        assertThat (lists.size (), equalTo (1));
        assertThat (lists.get (0).name, equalTo (userList.name));

        userList.name = "Watching";
        userListDao.updateList (userList);

        List<UserList> lists1 = userListDao.getUserLists ().blockingFirst ();
        assertThat (lists1.size (), equalTo (1));
        assertThat (lists1.get (0).name, equalTo (userList.name));
    }


    @Test
    public void deleteUserLists () {
        insertMovies ();

        UserList[] userLists = new UserList[10];
        for ( int i = 0; i < 10; i++ ) {
            UserList userList = new UserList ();
            userList.id = 800L + i;
            userList.name = "Watched" + i;
            userLists[i] = userList;
        }
        userListDao.insertAllLists (userLists);

        List<UserList> lists = userListDao.getUserLists ().blockingFirst ();
        assertThat (lists.size (), equalTo (10));

        userListDao.deleteList (userLists[1]);

        List<UserList> lists1 = userListDao.getUserLists ().blockingFirst ();
        assertThat (lists1.size (), equalTo (9));
        assertThat (lists1, not (hasItem (userLists[1])));
    }

    @Test
    public void anyListsExist () {
        insertMovies ();

        Boolean anyBefore = userListDao.anyListExists ().blockingGet ();
        assertThat (anyBefore, equalTo (false));

        UserList userList = new UserList ();
        userList.id = 800L;
        userList.name = "Watched";

        userListDao.insertList (userList);

        Boolean anyAfter = userListDao.anyListExists ().blockingGet ();
        assertThat (anyAfter, equalTo (true));
    }

    @Test
    public void addToListAndRead () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);

        userListDao.addToList (userListItem);

        List<Movie> items = userListDao.getListMovies (userListId).blockingGet ();
        assertThat (items.size (), equalTo (1));
        assertThat (items.get (0).id, equalTo (userListItem.movieId));
    }

    @Test
    public void loadListItems () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2);

        List<Movie> items = userListDao.getListMovies (userListId).blockingGet ();
        assertThat (items.size (), equalTo (3));
    }

    @Test
    public void removeFromList () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2);

        userListDao.removeFromList (userListItem1);

        List<Movie> items = userListDao.getListMovies (userListId).blockingGet ();
        assertThat (items.size (), CoreMatchers.equalTo (2));
        assertThat (items, not (hasItem (new Movie (userListItem1.movieId))));
    }

    @Test
    public void anyItemExists () {
        insertMovies ();
        long userListId = insertUserList ();

        boolean anyBefore = userListDao.anyItemExists (userListId);
        assertThat (anyBefore, equalTo (false));

        UserListItem userListItem = new UserListItem (userListId, 200L);

        userListDao.addToList (userListItem);

        boolean anyAfter = userListDao.anyItemExists (userListId);
        assertThat (anyAfter, equalTo (true));
    }

    @Test
    public void getTopMovieInList () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);
        UserListItem userListItem3 = new UserListItem (userListId, 600L);
        UserListItem userListItem4 = new UserListItem (userListId, 800L);
        UserListItem userListItem5 = new UserListItem (userListId, 900L);
        UserListItem userListItem6 = new UserListItem (userListId, 1000L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2, userListItem3,
                userListItem4, userListItem5, userListItem6);

        Movie item = userListDao.getTopListMovie (userListId).blockingGet ();
        assertThat (item.id, equalTo (1000L));
    }

    @Test
    public void isInList () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);

        boolean isInListBefore = userListDao.isInList (userListItem2.movieId, userListId);
        assertThat (isInListBefore, equalTo (false));

        userListDao.addAllToList (userListItem, userListItem1, userListItem2);

        boolean isInListAfter = userListDao.isInList (userListItem2.movieId, userListId);
        assertThat (isInListAfter, equalTo (true));
    }

    @Test
    public void deleteAllListItem () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);
        UserListItem userListItem3 = new UserListItem (userListId, 600L);
        UserListItem userListItem4 = new UserListItem (userListId, 800L);
        UserListItem userListItem5 = new UserListItem (userListId, 900L);
        UserListItem userListItem6 = new UserListItem (userListId, 1000L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2, userListItem3,
                userListItem4, userListItem5, userListItem6);

        boolean anyExistBefore = userListDao.anyItemExists (userListId);
        assertThat (anyExistBefore, equalTo (true));

        userListDao.deleteListItems (userListId);

        boolean anyExist = userListDao.anyItemExists (userListId);
        assertThat (anyExist, equalTo (false));
    }

    @Test
    public void copyItemsFromList () {
        insertMovies ();
        long userListId = insertUserList ();

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2);

        List<UserListItem> items = userListDao.copyItemFromOld (userListId);
        assertThat (items.size (), equalTo (3));
        assertThat (items.get (0), equalTo (userListItem));
    }

    @Test
    public void duplicateListItems () {
        insertMovies ();
        long userListId = 100L;
        UserList userList = new UserList ();
        userList.id = userListId;
        userList.name = "Watched";
        userListDao.insertList (userList);

        UserListItem userListItem = new UserListItem (userListId, 200L);
        UserListItem userListItem1 = new UserListItem (userListId, 300L);
        UserListItem userListItem2 = new UserListItem (userListId, 400L);

        userListDao.addAllToList (userListItem, userListItem1, userListItem2);

        long newListId = userListDao.duplicateListItems (userList);
        List<Movie> items = userListDao.getListMovies (newListId).blockingGet ();
        assertThat (items.size (), equalTo (3));
    }


    private long insertUserList () {
        long id = 100L;
        UserList userList = new UserList ();
        userList.name = "Watched";
        userList.id = id;
        userListDao.insertList (userList);
        return id;
    }


    private void insertMovies () {
        for ( int i = 0; i < 10; i++ ) {
            Movie movie = new Movie (100 + 100 * i);
            movie.voteAverage = i;
            movieDao.insertAll (movie);
        }
    }
}
