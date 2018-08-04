package com.winterparadox.themovieapp.common.beans;

import com.google.gson.annotations.SerializedName;

public class CrewMember {

    @SerializedName("gender")
    private int gender;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private Object profilePath;

    @SerializedName("id")
    private int id;

    @SerializedName("department")
    private String department;

    @SerializedName("job")
    private String job;

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