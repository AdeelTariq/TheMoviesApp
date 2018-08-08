package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.base.BasePresenter;
import com.winterparadox.themovieapp.common.beans.Person;

public abstract class PersonDetailsPresenter extends BasePresenter<PersonDetailsView> {

    protected Person person;

    public void attachView (PersonDetailsView view, Person person) {
        this.person = person;
        super.attachView (view);
    }
}
