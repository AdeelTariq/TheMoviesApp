package com.winterparadox.themovieapp.common.room;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import java.util.List;

import androidx.annotation.VisibleForTesting;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public abstract class UserListDao {

    @Insert(onConflict = IGNORE)
    public abstract Long insertList (UserList list);

    @Insert(onConflict = IGNORE)
    public abstract Long[] insertAllLists (UserList... list);

    @Insert(onConflict = REPLACE)
    public abstract Long updateList (UserList list);

    @Delete
    public abstract void deleteList (UserList list);

    @Query("SELECT * FROM UserList")
    public abstract Flowable<List<UserList>> getUserLists ();

    @Query("SELECT * FROM UserList")
    public abstract Single<List<UserList>> getUserListsOnce ();

    @Query("SELECT EXISTS(SELECT 1 FROM UserList)")
    public abstract Single<Boolean> anyListExists ();


    @Insert(onConflict = REPLACE)
    public abstract Long addToList (UserListItem listItem);

    @Query("SELECT * FROM Movie INNER JOIN UserListItem ON Movie.id=UserListItem.movieId " +
            " WHERE UserListItem.userListId= :userListId ORDER BY UserListItem.`order`")
    public abstract Single<List<Movie>> getListMovies (long userListId);

    @Insert(onConflict = REPLACE)
    public abstract Long[] addAllToList (UserListItem... listItem);

    @Delete
    public abstract void removeFromList (UserListItem listItem);


    @Query("SELECT EXISTS(SELECT 1 FROM UserListItem WHERE UserListItem.userListId= :userListId)")
    public abstract boolean anyItemExists (long userListId);

    @Query("SELECT * FROM Movie INNER JOIN UserListItem ON Movie.id=UserListItem.movieId " +
            " WHERE UserListItem.userListId= :userListId ORDER BY voteAverage DESC LIMIT 1")
    public abstract Single<Movie> getTopListMovie (long userListId);


    @Query("SELECT EXISTS(SELECT 1 FROM UserListItem WHERE UserListItem.movieId = :movieId" +
            " AND UserListItem.userListId = :listId)")
    public abstract boolean isInList (long movieId, long listId);

    @Query("DELETE FROM UserListItem WHERE userListId = :listId")
    public abstract void deleteListItems (long listId);

    // for transaction
    @VisibleForTesting
    @Query("SELECT * FROM UserListItem WHERE userListId = :oldId;")
    public abstract List<UserListItem> copyItemFromOld (long oldId);

    // ---

    @Transaction
    public long duplicateListItems (UserList list) {
        Long newId = insertList (new UserList (list.name + " copy"));

        List<UserListItem> userListItems = copyItemFromOld (list.id);
        for ( UserListItem userListItem : userListItems ) {
            userListItem.userListId = newId;
            addToList (userListItem);
        }
        return newId;
    }
}
