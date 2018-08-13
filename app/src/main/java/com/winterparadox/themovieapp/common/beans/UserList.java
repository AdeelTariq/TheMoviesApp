package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class UserList implements Serializable {

    public static final int USERLIST_WATCHLIST = 0,
            USERLIST_WATCHED = 1,
            USERLIST_COLLECTION = 2;

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @Ignore
    public String backDropPath;

    @Ignore
    public boolean isAdded;

    public UserList () {
    }

    @Ignore
    public UserList (String name) {
        this.name = name;
    }

    @Ignore
    public UserList (int id, String name) {

        this.id = id;
        this.name = name;
    }
}
