package com.winterparadox.themovieapp.common.beans;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(primaryKeys = {"movieId", "userListId"},
        indices = {@Index(value = "order")})
public class UserListItem {

    public int order;

    public long movieId;

    public long userListId;

    public UserListItem () {
    }

    @Ignore
    public UserListItem (long userListId, long movieId) {
        this.userListId = userListId;
        this.order = Integer.MAX_VALUE;
        this.movieId = movieId;
    }
}
