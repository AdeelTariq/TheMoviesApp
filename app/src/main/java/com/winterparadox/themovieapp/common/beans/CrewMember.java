package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CrewMember extends Person {

    @SerializedName("department")
    public String department;

    @SerializedName("job")
    public String job;

    @Override
    public String toString () {
        return
                "CrewMember{" +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",department = '" + department + '\'' +
                        ",job = '" + job + '\'' +
                        "}";
    }
}