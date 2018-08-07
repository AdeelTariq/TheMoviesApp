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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.common.views.TransitionNames;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static android.support.transition.TransitionSet.ORDERING_TOGETHER;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.LARGE_PROFILE;

public class PersonDetailsFragment extends Fragment implements PersonDetailsView {

    private static final String PERSON = "person";
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.ivProfile) ImageView ivProfile;
    @BindView(R.id.tvBio) TextView tvBio;
    @BindView(R.id.tvKnownFor) TextView tvKnownFor;
    @BindView(R.id.tvCredits) TextView tvCredits;
    @BindView(R.id.rvCredits) ShimmerRecyclerView rvCredits;
    Unbinder unbinder;
    @BindView(R.id.shimmerLayout) ShimmerLayout shimmerLayout;
    private Person person;

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
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_person_details, container, false);
        unbinder = ButterKnife.bind (this, view);


        RequestOptions requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new RoundedCorners (4));

        tvName.setText (person.name);
        tvName.setTransitionName (TransitionNames.PERSON_NAME + person.name);

        ivProfile.setTransitionName (TransitionNames.PERSON_PROFILE + person.name);

        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + LARGE_PROFILE + person.profilePath))
                .apply (requestOptions)
                .into (ivProfile);

        showProgress ();

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        unbinder.unbind ();
    }

    @Override
    public void showAdditionalDetails (String knownFor, String creditCount, List<Movie>
            topCredits) {

    }

    @Override
    public void showProgress () {
        shimmerLayout.startShimmerAnimation ();
    }

    @Override
    public void hideProgress () {
        shimmerLayout.stopShimmerAnimation ();
    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {

    }
}
