package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.arch.BasePresenter;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;

public abstract class PersonDetailsPresenter extends BasePresenter<PersonDetailsView> {

    protected Person person;

    public void attachView (PersonDetailsView view, Person person, Navigator navigator) {
        this.person = person;
        super.attachView (view, navigator);
    }

    public abstract void onMovieClicked (Movie movie, Object element);
}
