package com.winterparadox.themovieapp.movieDetails;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Barrier;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.UiUtils;
import com.winterparadox.themovieapp.common.beans.Movie;

import javax.inject.Inject;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.supercharge.shimmerlayout.ShimmerLayout;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class MovieDetailsFragment extends Fragment implements MovieDetailsView {

    private static final String MOVIE = "movie";
    @BindView(R.id.ivBackdrop) ImageView ivBackdrop;
    @BindView(R.id.ivPoster) ImageView ivPoster;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvYear) TextView tvYear;
    @BindView(R.id.tvPlot) TextView tvPlot;
    @BindView(R.id.circularVotes) CircularProgressIndicator circularVotes;
    @BindView(R.id.barrier2) Barrier barrier2;
    @BindView(R.id.btnFavorite) ImageButton btnFavorite;
    @BindView(R.id.captionFav) TextView captionFav;
    @BindView(R.id.btnAdd) ImageButton btnAdd;
    @BindView(R.id.captionAdd) TextView captionAdd;
    @BindView(R.id.tvInfo) TextView tvInfo;
    @BindView(R.id.shimmerLayout) ShimmerLayout shimmerLayout;

    private Movie movie;
    private RequestOptions requestOptions;
    Unbinder unbinder;

    @Inject MovieDetailsPresenter presenter;

    public static MovieDetailsFragment instance (Movie movie) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (MOVIE, movie);
        movieDetailsFragment.setArguments (args);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        Bundle arguments = getArguments ();
        movie = (Movie) arguments.getSerializable (MOVIE);

        requestOptions = new RequestOptions ()
                .transforms (new CenterCrop (), new RoundedCorners (4));
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind (this, view);

        presenter.attachView (this, movie);

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovie (Movie movie, String year) {
        RequestOptions requestOptions1 = new RequestOptions ()
                .transforms (new CenterCrop (),
                        new RoundedCornersTransformation ((int) UiUtils.dpToPx (24), 0,
                                RoundedCornersTransformation.CornerType.TOP));
        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + movie.backdropPath))
                .apply (requestOptions1)
                .into (ivBackdrop);

        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .apply (requestOptions)
                .into (ivPoster);

        tvTitle.setText (movie.title);
        tvYear.setText (year);
        tvPlot.setText (movie.overview);
        circularVotes.setProgress (movie.voteAverage, 10);

    }

    @Override
    public void showAdditionalDetails (Movie movie, String ageRating, String runtime,
                                       String genres) {

        tvInfo.setText (String.format ("%s\t\t|\t\t%s\t\t|\t\t%s\n%s", ageRating, runtime,
                movie.originalLanguage, genres));

    }

    @OnClick({R.id.btnAdd, R.id.captionAdd})
    void addToList () {

    }

    @OnClick({R.id.btnFavorite, R.id.captionFav})
    void favorite () {

    }


    @Override
    public void showProgress () {
        shimmerLayout.startShimmerAnimation ();
    }

    @Override
    public void hideProgress () {
        shimmerLayout.stopShimmerAnimation ();
        tvInfo.setBackground (null);
    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {

    }
}
