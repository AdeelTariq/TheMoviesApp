package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

@Entity(primaryKeys = {"movieId", "userListId"},
        indices = {@Index(value = "order")})
public class UserListItem {

    public int order;

    public int movieId;

    public int userListId;

    public UserListItem () {
    }

    @Ignore
    public UserListItem (int userListId, int movieId) {
        this.userListId = userListId;
        this.order = Integer.MAX_VALUE;
        this.movieId = movieId;
    }
}
