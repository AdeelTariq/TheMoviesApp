package com.winterparadox.themovieapp.personDetails;

import com.winterparadox.themovieapp.arch.BaseView;
import com.winterparadox.themovieapp.common.beans.Image;
import com.winterparadox.themovieapp.common.beans.Movie;

import java.util.List;

public interface PersonDetailsView extends BaseView {

    void showAdditionalDetails (String bio, String knownFor,
                                String creditCount, List<Movie> topCredits);

    void showImages (List<Image> all);
}
