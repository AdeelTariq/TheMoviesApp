package com.winterparadox.themovieapp.common.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.beans.UserListItem;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface UserListDao {

    @Query("SELECT * FROM UserList")
    Single<List<UserList>> getUserLists ();

    @Query("SELECT EXISTS(SELECT 1 FROM UserList)")
    Single<Boolean> anyExists ();

    @Query("SELECT * FROM Movie INNER JOIN UserListItem ON Movie.id=UserListItem.movieId " +
            " INNER JOIN UserList ON UserListItem.userListId= :userListId ORDER BY voteAverage " +
            "LIMIT 1")
    Single<Movie> getTopListMovie (int userListId);

    @Insert(onConflict = IGNORE)
    Long insert (UserList list);


    @Insert(onConflict = REPLACE)
    Long update (UserList list);

    @Insert(onConflict = IGNORE)
    Long addToList (UserListItem listItem);

    @Delete
    void removeFromList (UserListItem list);

    @Query("SELECT EXISTS(SELECT 1 FROM UserListItem WHERE UserListItem.movieId = :movieId" +
            " AND UserListItem.userListId = :listId)")
    boolean isInList (int movieId, int listId);
}
