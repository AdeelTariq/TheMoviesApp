package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.beans.Person;

import io.reactivex.Single;

public interface PersonApiInteractor {

    Single<Person> personDetails (int id);
}
