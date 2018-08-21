package com.winterparadox.themovieapp.home;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_FAVORITES;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_POPULAR;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_RECENT;
import static com.winterparadox.themovieapp.common.beans.HomeSection.SECTION_UPCOMING;

public class HomeFragment extends Fragment implements HomeView, HomeMoviesAdapter.ClickListener {

    @Inject HomePresenter presenter;

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;
    private HomeMoviesAdapter moviesAdapter;

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        presenter.attachView (this, (Navigator) getActivity ());


        boolean isLandscape = false;

        // setting up grid layout manager
        GridLayoutManager gridLayoutManager;

        if ( getActivity ().getResources ().getConfiguration ().orientation == Configuration
                .ORIENTATION_PORTRAIT ) {

            gridLayoutManager = new GridLayoutManager (getActivity (), 3);
            gridLayoutManager.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup () {
                @Override
                public int getSpanSize (int position) {
                    if ( position % 5 == 0 ) {
                        return 3; //item will take full row
                    } else if ( position % 5 == 1 ) {
                        return 3; //you will have full row
                    } else {
                        return 1; //you will have 1/3 row size item
                    }
                }
            });

        } else {
            gridLayoutManager = new GridLayoutManager (getActivity (), 4);
            gridLayoutManager.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup () {
                @Override
                public int getSpanSize (int position) {
                    if ( position % 5 == 0 ) {
                        return 4; //item will take full row
                    } else {
                        return 1; //you will have 1/4 row size item
                    }
                }
            });

            isLandscape = true;
        }

        recyclerView.setLayoutManager (gridLayoutManager);

        // setting divider space
        HomeItemDecoration dividerItemDecoration = new HomeItemDecoration (getActivity (),
                gridLayoutManager.getOrientation (), gridLayoutManager.getSpanCount ());
        dividerItemDecoration.setMiddleHorizontalPadding (8);
        dividerItemDecoration.setVerticalOffset (8);
        dividerItemDecoration.setDefaultOffset (24);
        recyclerView.addItemDecoration (dividerItemDecoration);

        recyclerView.setGridChildCount (1);

        recyclerView.setItemViewCacheSize (20);
        recyclerView.setDrawingCacheEnabled (true);


        moviesAdapter = new HomeMoviesAdapter (this, presenter::fetchData, isLandscape);
        recyclerView.setAdapter (moviesAdapter);

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

        presenter.fetchData ();

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void onResume () {
        super.onResume ();
        NetworkUtils.registerConnectivityCallback (getActivity (), callback);
    }

    @Override
    public void onPause () {
        NetworkUtils.unregisterConnectivityCallback (getActivity (), callback);
        super.onPause ();
    }


    ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback () {
        @Override
        public void onAvailable (Network network) {
            if ( getActivity () == null ) {
                return;
            }
            if ( !NetworkUtils.isConnected (getActivity ()) ) {
                return;
            }

            if ( moviesAdapter.getItemCount () > 4 ) {
                return;
            }

            getActivity ().runOnUiThread (() -> presenter.fetchData ());
        }
    };

    @Override
    public void clearView () {
        moviesAdapter.clearItems ();
    }

    @Override
    public void showMovies (ArrayList<Object> movies) {

        ChangeBounds transition = new ChangeBounds ();
        Fade transition2 = new Fade ();
        TransitionSet transitionSet = new TransitionSet ().addTransition (transition)
                .addTransition (transition2);
        transitionSet.setInterpolator (new AccelerateInterpolator (1));
        transitionSet.setDuration (300);
        TransitionManager.beginDelayedTransition ((ViewGroup) tvHeader.getParent (), transitionSet);

        moviesAdapter.addMovies (movies);
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        presenter.onMovieClicked (movie, element);
    }

    @Override
    public void onSubHeaderClick (int header) {
        switch ( header ) {
            case SECTION_FAVORITES:
                presenter.onFavoritesClicked ();
                break;
            case SECTION_RECENT:
                presenter.onRecentlyViewedClicked ();
                break;
            case SECTION_POPULAR:
                presenter.onChartClicked (new Chart (CHART_POPULAR, getString (R.string.popular)));
                break;
            case SECTION_UPCOMING:
                presenter.onChartClicked (new Chart (CHART_LATEST, getString (R.string.latest)));
                break;
        }
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

    }

    @Override
    public void showError (String message) {
        moviesAdapter.setError (message);
    }

    @Override
    public String popularTitle () {
        return getString (R.string.popular_title);
    }

    @Override
    public String upcomingTitle () {
        return getString (R.string.upcoming_title);
    }

    @Override
    public String recentlyTitle () {
        return getString (R.string.recently_viewed);
    }

    @Override
    public String favoriteTitle () {
        return getString (R.string.favorites);
    }
}
