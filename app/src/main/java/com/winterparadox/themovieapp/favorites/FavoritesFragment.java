package com.winterparadox.themovieapp.favorites;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavoritesFragment extends Fragment implements FavoritesView,
        FavoritesAdapter.ClickListener {

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;

    @Inject FavoritesPresenter presenter;
    private FavoritesAdapter movieAdapter;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (R.string.favorites);

        recyclerView.hideShimmerAdapter ();

        view.postDelayed (() -> {

            if ( getActivity () == null ) {
                return;
            }

            GridLayoutManager gridLayoutManager;

            boolean isLargeTablet = getResources ().getBoolean (R.bool.isLargeTablet);

            if ( getActivity ().getResources ().getConfiguration ().orientation == Configuration
                    .ORIENTATION_PORTRAIT ) {
                gridLayoutManager = new GridLayoutManager (getActivity (),
                        1 + (isLargeTablet ? 1 : 0));

            } else {
                gridLayoutManager = new GridLayoutManager (getActivity (),
                        2 + (isLargeTablet ? 1 : 0));
            }

            recyclerView.setLayoutManager (gridLayoutManager);
            DefaultListDecoration decor = new DefaultListDecoration (getActivity (),
                    DividerItemDecoration.VERTICAL, gridLayoutManager.getSpanCount ());
            decor.setDefaultOffset (24);
            decor.setItemPadding (8);
            recyclerView.addItemDecoration (decor);
            recyclerView.setHasFixedSize (true);

            recyclerView.setItemViewCacheSize (20);
            recyclerView.setDrawingCacheEnabled (true);

            movieAdapter = new FavoritesAdapter (this);
            recyclerView.setAdapter (movieAdapter);

            recyclerView.addOnScrollListener (new OnScrollObserver () {
                @Override
                public void onScrolling () {
                    scrollIndicator.setVisibility (View.VISIBLE);
                }

                @Override
                public void onScrollToTop () {
                    scrollIndicator.setVisibility (View.GONE);
                }
            });

            recyclerView.setDemoLayoutReference (R.layout.layout_movie_list_shimmer_item);
            recyclerView.setDemoChildCount (10);
            recyclerView.setDemoLayoutManager (ShimmerRecyclerView.LayoutMangerType.GRID);
            recyclerView.setGridChildCount (gridLayoutManager.getSpanCount ());

            presenter.attachView (this, (Navigator) getActivity ());
        }, 300);

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        if ( recyclerView.getLayoutManager () != null ) {
            presenter.saveState (((GridLayoutManager) recyclerView.getLayoutManager ())
                    .findLastCompletelyVisibleItemPosition ());
        }
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies, int lastVisibleItem) {

        ChangeBounds transition = new ChangeBounds ();
        Fade transition2 = new Fade ();
        TransitionSet transitionSet = new TransitionSet ().addTransition (transition)
                .addTransition (transition2);
        transitionSet.setInterpolator (new AccelerateInterpolator (1));
        transitionSet.setDuration (300);
        TransitionManager.beginDelayedTransition ((ViewGroup) tvHeader.getParent (), transitionSet);

        movieAdapter.setItems (movies);

        recyclerView.post (() -> recyclerView.getLayoutManager ()
                .scrollToPosition (lastVisibleItem));
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        presenter.onMovieClicked (movie, element);
    }

    @Override
    public void unFavoriteClick (Movie movie) {
        presenter.unFavorite (movie);
    }

    @Override
    public void showProgress () {
        recyclerView.showShimmerAdapter ();
    }

    @Override
    public void hideProgress () {
        recyclerView.hideShimmerAdapter ();
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

    @Override
    public void showError (String message) {

    }
}
