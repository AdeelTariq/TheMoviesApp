package com.winterparadox.themovieapp.charts.chartMovieList;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;
import com.winterparadox.themovieapp.search.HostView;

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
        movieListAdapter = new ChartMovieListAdapter (this);
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

        presenter.attachView (this, chart);


        return view;
    }

    // todo show progres
    // pagination

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies) {
        movieListAdapter.setItems (movies);
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        ((HostView) getActivity ()).openMovie (movie, element);
    }

    @Override
    public void showProgress () {

    }

    @Override
    public void hideProgress () {

    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {
        // todo show error as adapter item
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }
}
