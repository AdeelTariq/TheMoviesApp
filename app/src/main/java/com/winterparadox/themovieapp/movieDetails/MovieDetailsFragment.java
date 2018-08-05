package com.winterparadox.themovieapp.movieDetails;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Barrier;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.GlideApp;
import com.winterparadox.themovieapp.common.beans.CastMember;
import com.winterparadox.themovieapp.common.beans.CrewMember;
import com.winterparadox.themovieapp.common.beans.Movie;

import javax.inject.Inject;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.supercharge.shimmerlayout.ShimmerLayout;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.IMAGE;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.MEDIUM_BACKDROP;
import static com.winterparadox.themovieapp.common.retrofit.ApiBuilder.SMALL_POSTER;

public class MovieDetailsFragment extends Fragment implements MovieDetailsView,
        MoviesDetailsCastAdapter.ClickListener, MoviesDetailsCrewAdapter.ClickListener,
        MoviesDetailsMoviesAdapter.ClickListener {

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
    @BindView(R.id.rvCast) ShimmerRecyclerView rvCast;
    @BindView(R.id.rvCrew) RecyclerView rvCrew;
    @BindView(R.id.rvSimilar) RecyclerView rvSimilar;

    private Movie movie;
    private RequestOptions requestOptions;
    Unbinder unbinder;

    @Inject MovieDetailsPresenter presenter;
    private MoviesDetailsCastAdapter castAdapter;
    private MoviesDetailsCrewAdapter crewAdapter;
    private MoviesDetailsMoviesAdapter movieAdapter;

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


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager (getActivity (),
                HORIZONTAL, false);
        rvCast.setLayoutManager (linearLayoutManager1);
        CastItemDecoration decor = new CastItemDecoration (getActivity (), DividerItemDecoration
                .HORIZONTAL);
        decor.setDefaultOffset (8);
        decor.setLastItemEndOffset (24);
        rvCast.addItemDecoration (decor);
        castAdapter = new MoviesDetailsCastAdapter (this);
        rvCast.setAdapter (castAdapter);


        LinearLayoutManager gridManager = new GridLayoutManager (getActivity (), 2,
                HORIZONTAL, false);
        rvCrew.setLayoutManager (gridManager);
        CrewItemDecoration decorCrew = new CrewItemDecoration (getActivity (),
                DividerItemDecoration.HORIZONTAL);
        decorCrew.setDefaultOffset (16);
        decorCrew.setHorizontal (48);
        rvCrew.addItemDecoration (decorCrew);
        crewAdapter = new MoviesDetailsCrewAdapter (this);
        rvCrew.setAdapter (crewAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager (getActivity (),
                HORIZONTAL, false);
        rvSimilar.setLayoutManager (linearLayoutManager2);
        rvSimilar.addItemDecoration (decor);
        movieAdapter = new MoviesDetailsMoviesAdapter (this);
        rvSimilar.setAdapter (movieAdapter);


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
        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + MEDIUM_BACKDROP + movie.backdropPath))
                .centerCrop ()
                .into (ivBackdrop);

        GlideApp.with (getActivity ())
                .load (Uri.parse (IMAGE + SMALL_POSTER + movie.posterPath))
                .apply (requestOptions)
                .into (ivPoster);

        tvTitle.setText (movie.title);
        tvYear.setText (year);
        tvPlot.setText (movie.overview);
        circularVotes.setProgress (movie.voteAverage * 10, 100);

    }

    @Override
    public void showAdditionalDetails (Movie movie, String ageRating, String runtime,
                                       String genres) {

        tvInfo.setText (String.format ("%s\t\t|\t\t%s\t\t|\t\t%s\n%s", ageRating, runtime,
                movie.originalLanguage, genres));

        castAdapter.setItems (movie.credits.cast);
        crewAdapter.setItems (movie.credits.crew);
        movieAdapter.setItems (movie.similar.movies);
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
        rvCast.showShimmerAdapter ();
    }

    @Override
    public void hideProgress () {
        shimmerLayout.stopShimmerAnimation ();
        rvCast.hideShimmerAdapter ();
        tvInfo.setBackground (null);
    }

    @Override
    public void onCastClick (CastMember member) {

    }

    @Override
    public void onCrewClick (CrewMember member) {

    }

    @Override
    public void onMovieClick (Movie member) {

    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {

    }
}
