package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.common.base.BaseView;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface PersonDetailsView extends BaseView {

    void showAdditionalDetails (String bio, String knownFor,
                                String creditCount, List<Movie> topCredits);
}
