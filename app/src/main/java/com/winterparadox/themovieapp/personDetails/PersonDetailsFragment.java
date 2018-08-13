package com.winterparadox.themovieapp.personDetails;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Fade;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Image;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.common.views.DefaultHorizontalItemDecoration;
import com.winterparadox.themovieapp.common.views.HorizontalMoviesAdapter;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static android.support.transition.TransitionSet.ORDERING_TOGETHER;
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.LARGE_PROFILE;

public class PersonDetailsFragment extends Fragment implements PersonDetailsView,
        HorizontalMoviesAdapter.ClickListener, ImagesAdapter.ClickListener {

    private static final String PERSON = "person";
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.ivProfile) ImageView ivProfile;
    @BindView(R.id.tvBio) TextView tvBio;
    @BindView(R.id.tvKnownFor) TextView tvKnownFor;
    @BindView(R.id.tvCredits) TextView tvCredits;
    @BindView(R.id.rvCredits) ShimmerRecyclerView rvCredits;
    @BindView(R.id.shimmerBio) ShimmerLayout shimmerLayout;
    Unbinder unbinder;
    @BindView(R.id.captionImages) TextView captionImages;
    @BindView(R.id.rvImages) RecyclerView rvImages;
    private Person person;
    private HorizontalMoviesAdapter movieAdapter;

    @Inject PersonDetailsPresenter presenter;
    private ImagesAdapter imageAdapter;

    public static PersonDetailsFragment instance (Person person) {
        PersonDetailsFragment personDetailsFragment = new PersonDetailsFragment ();


        TransitionSet transition = new TransitionSet ()
                .setOrdering (ORDERING_TOGETHER)
                .addTransition (new ChangeBounds ())
                .addTransition (new ChangeImageTransform ())
                .addTransition (new ChangeTransform ());
        personDetailsFragment.setSharedElementEnterTransition (transition);
        personDetailsFragment.setSharedElementReturnTransition (new Fade ());


        Bundle args = new Bundle ();
        args.putSerializable (PERSON, person);
        personDetailsFragment.setArguments (args);
        return personDetailsFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        person = (Person) getArguments ().getSerializable (PERSON);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_person_details, container, false);
        unbinder = ButterKnife.bind (this, view);


        RequestOptions requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new RoundedCorners (8));

        tvName.setText (person.name);
        tvName.setTransitionName (TransitionNames.PERSON_NAME + person.name);

        ivProfile.setTransitionName (TransitionNames.PERSON_PROFILE + person.name);

        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + LARGE_PROFILE + person.profilePath))
                .apply (requestOptions)
                .into (ivProfile);


        DefaultHorizontalItemDecoration decor = new DefaultHorizontalItemDecoration (getActivity
                (), DividerItemDecoration
                .HORIZONTAL);
        decor.setDefaultOffset (8);
        decor.setLastItemEndOffset (24);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getActivity (),
                HORIZONTAL, false);
        rvCredits.setLayoutManager (linearLayoutManager);
        rvCredits.addItemDecoration (decor);
        rvCredits.setHasFixedSize (true);
        rvCredits.setItemViewCacheSize (10);
        rvCredits.setDrawingCacheEnabled (true);
        movieAdapter = new HorizontalMoviesAdapter (this);
        rvCredits.setAdapter (movieAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager (getActivity (),
                HORIZONTAL, false);
        rvImages.setLayoutManager (linearLayoutManager2);
        rvImages.addItemDecoration (decor);
        rvImages.setItemViewCacheSize (10);
        rvImages.setDrawingCacheEnabled (true);
        imageAdapter = new ImagesAdapter (this);
        rvImages.setAdapter (imageAdapter);



        presenter.attachView (this, person, ((Navigator) getActivity ()));

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showAdditionalDetails (String bio, String knownFor,
                                       String creditCount, List<Movie> topCredits) {
        tvBio.setText (bio);
        tvKnownFor.setText (knownFor);
        tvCredits.setText (creditCount);

        movieAdapter.setItems (topCredits);
    }

    @Override
    public void showImages (List<Image> all) {
        captionImages.setVisibility (View.VISIBLE);
        rvImages.setVisibility (View.VISIBLE);

        imageAdapter.setItems (all);
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        presenter.onMovieClicked (movie, element);
    }

    @Override
    public void onImageClick (Image image, View element) {

    }

    @Override
    public void showProgress () {
        shimmerLayout.startShimmerAnimation ();
        rvCredits.showShimmerAdapter ();
    }

    @Override
    public void hideProgress () {
        shimmerLayout.stopShimmerAnimation ();
        rvCredits.hideShimmerAdapter ();
        tvBio.setBackground (null);
        tvKnownFor.setBackground (null);
        tvCredits.setBackground (null);
    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {

    }
}
