package com.winterparadox.themovieapp.common.beans;

import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    @Override
    public boolean equals (@Nullable Object obj) {
        if ( obj instanceof UserList ) {
            return name.equals (((UserList) obj).name);
        }
        return super.equals (obj);
    }
}
