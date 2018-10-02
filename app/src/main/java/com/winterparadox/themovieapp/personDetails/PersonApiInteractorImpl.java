package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.beans.Person;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class PersonApiInteractorImpl implements PersonApiInteractor {

    private PersonApiService service;

    public PersonApiInteractorImpl (PersonApiService service) {

        this.service = service;
    }

    @Override
    public Single<Person> personDetails (long id) {
        return service.personDetails (id)
                .subscribeOn (Schedulers.io ());
    }
}
