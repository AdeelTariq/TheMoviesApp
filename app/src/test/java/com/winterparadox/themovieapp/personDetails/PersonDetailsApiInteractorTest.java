package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.beans.Person;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class PersonDetailsApiInteractorTest {

    @Mock PersonApiService apiService;

    PersonApiInteractor interactor;

    @Before
    public void setup () {
        MockitoAnnotations.initMocks (this);
        interactor = new PersonApiInteractorImpl (apiService);
    }

    @Test
    public void movieDetails_shouldReturnFromPersonService () {
        Person person = new Person ();
        person.id = 800L;

        TestObserver<Person> subscriber = new TestObserver<> ();
        given (apiService.personDetails (anyLong ()))
                .willReturn (Single.just (person));

        interactor.personDetails (person.id).subscribe (subscriber);

        then (apiService).should ().personDetails (person.id);
        subscriber.awaitDone (5000, TimeUnit.MILLISECONDS);
        subscriber.assertValue (person);
    }
}
