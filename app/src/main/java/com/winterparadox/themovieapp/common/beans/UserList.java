package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = @Index(value = "name"))
public class UserList implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

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
}
