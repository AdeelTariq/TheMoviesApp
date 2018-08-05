package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CrewMember {

    @SerializedName("gender")
    public int gender;

    @SerializedName("credit_id")
    public String creditId;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_path")
    public Object profilePath;

    @SerializedName("id")
    public int id;

    @SerializedName("department")
    public String department;

    @SerializedName("job")
    public String job;

    @Override
    public String toString () {
        return
                "CrewMember{" +
                        "gender = '" + gender + '\'' +
                        ",credit_id = '" + creditId + '\'' +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",department = '" + department + '\'' +
                        ",job = '" + job + '\'' +
                        "}";
    }
}