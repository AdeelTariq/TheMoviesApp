package com.winterparadox.themovieapp.charts.chartMovieList;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.EndlessRecyclerViewScrollListener;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.widget.GridLayout.VERTICAL;

public class ChartMovieListFragment extends Fragment implements ChartMovieListView,
        ChartMovieListAdapter.ClickListener {

    private static final String CHART = "chart";
    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;
    private Chart chart;
    private ChartMovieListAdapter movieListAdapter;

    @Inject ChartMovieListPresenter presenter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static ChartMovieListFragment instance (Chart chart) {
        ChartMovieListFragment chartMovieListFragment = new ChartMovieListFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (CHART, chart);
        chartMovieListFragment.setArguments (args);
        return chartMovieListFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        chart = (Chart) getArguments ().getSerializable (CHART);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (chart.name);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getActivity (),
                VERTICAL, false);
        recyclerView.setLayoutManager (linearLayoutManager);
        DefaultListDecoration decor = new DefaultListDecoration (getActivity (),
                DividerItemDecoration.VERTICAL);
        decor.setDefaultOffset (24);
        decor.setItemPadding (8);
        recyclerView.addItemDecoration (decor);
        recyclerView.setHasFixedSize (true);

        recyclerView.setDemoChildCount (10);
        recyclerView.setDemoLayoutReference (R.layout.layout_movie_list_shimmer_item);

        movieListAdapter = new ChartMovieListAdapter (this, presenter::fetchData);

        recyclerView.setAdapter (movieListAdapter);

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


        presenter.attachView (this, chart, (Navigator) getActivity ());

        scrollListener = new EndlessRecyclerViewScrollListener (linearLayoutManager) {
            @Override
            public void onLoadMore (int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchDataPage (page);
            }
        };
        recyclerView.addOnScrollListener (scrollListener);

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies) {
        movieListAdapter.setItems (movies);
    }

    @Override
    public void addMovies (List<Movie> movies) {
        movieListAdapter.addItems (movies);
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        presenter.onMovieClicked (movie, element);
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
    public void showPageProgress () {
        movieListAdapter.setProgress (true);
    }

    @Override
    public void hidePageProgress () {
        movieListAdapter.setProgress (false);
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

    @Override
    public void showError (String message) {
        movieListAdapter.setError (message);
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

            if ( movieListAdapter.getItemCount () > 4 ) {
                return;
            }

            getActivity ().runOnUiThread (() -> {
                scrollListener.resetState ();
                presenter.fetchData ();
            });
        }
    };

}
