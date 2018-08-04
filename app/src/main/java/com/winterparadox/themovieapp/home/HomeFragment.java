package com.winterparadox.themovieapp.home;

import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;
import com.winterparadox.themovieapp.search.HostView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

        presenter.attachView (this);

        // setting up grid layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager (getActivity (), 3);
        gridLayoutManager.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup () {
            @Override
            public int getSpanSize (int position) {
                //define span size for this position
                //some example for your first three items
                if ( position % 5 == 0 ) {
                    return 3; //item will take full row
                } else if ( position % 5 == 1 ) {
                    return 3; //you will have full row
                } else {
                    return 1; //you will have 1/3 row size item
                }
            }
        });
        recyclerView.setLayoutManager (gridLayoutManager);

        // setting divider space
        HomeItemDecoration dividerItemDecoration = new HomeItemDecoration (getActivity (),
                gridLayoutManager.getOrientation ());
        dividerItemDecoration.middleHorizontalPadding = 8;
        dividerItemDecoration.verticalOffset = 8;
        dividerItemDecoration.setDefaultOffset (24);
        dividerItemDecoration.setDrawable (new ColorDrawable (0x00000000));
        recyclerView.addItemDecoration (dividerItemDecoration);

        moviesAdapter = new HomeMoviesAdapter (this);
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
        moviesAdapter.addMovies (movies);
    }

    @Override
    public void onMovieClick (Movie movie) {
        ((HostView) getActivity ()).openMovie (movie);
    }

    @Override
    public void onSubHeaderClick (int header) {

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
        moviesAdapter.addError (message);
    }

    @Override
    public String popularTitle () {
        return getString (R.string.popular_title);
    }

    @Override
    public String latestTitle () {
        return getString (R.string.latest_title);
    }
}
