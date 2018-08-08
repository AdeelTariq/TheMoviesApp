package com.winterparadox.themovieapp.common.beans;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.winterparadox.themovieapp.common.Singleton;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {

    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("known_for_department")
    public String knownFor;

    @SerializedName("biography")
    public String bio;

    @SerializedName("popularity")
    public double popularity;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_path")
    public String profilePath;

    @SerializedName("id")
    public int id;

    @SerializedName("adult")
    public boolean adult;

    @TypeConverters(Person.class)
    @SerializedName("movie_credits")
    public MovieCredits credits;


    public static class MovieCredits {
        public List<Movie> cast;
        public List<Movie> crew;
    }

    @Override
    public String toString () {
        return
                "Person{" +
                        "media_type = '" + mediaType + '\'' +
                        ",known_for = '" + knownFor + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",name = '" + name + '\'' +
                        ",profile_path = '" + profilePath + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        "}";
    }


    @TypeConverter
    public static MovieCredits storedStringToCredits (String value) {
        return Singleton.gson.fromJson (value, new TypeToken<MovieCredits> () {
        }.getType ());
    }

    @TypeConverter
    public static String creditsToStoredString (MovieCredits movieCredits) {
        return Singleton.gson.toJson (movieCredits);
    }

}