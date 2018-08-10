package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.beans.Person;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PersonApiService {

    @GET("person/{id}?append_to_response=movie_credits,tagged_images")
    Single<Person> personDetails (@Path("id") int personId);
}
