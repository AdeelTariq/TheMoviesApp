package com.winterparadox.themovieapp.charts;

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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;
import com.winterparadox.themovieapp.search.HostView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.widget.GridLayout.VERTICAL;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_LATEST;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_POPULAR;
import static com.winterparadox.themovieapp.common.beans.Chart.CHART_TOP_RATED;

public class ChartsFragment extends Fragment implements ChartsView, ChartsAdapter.ClickListener {

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;
    private ChartsAdapter chartsAdapter;

    @Inject ChartsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (R.string.charts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getActivity (),
                VERTICAL, false);
        recyclerView.setLayoutManager (linearLayoutManager);
        DefaultListDecoration decor = new DefaultListDecoration (getActivity (),
                DividerItemDecoration.VERTICAL);
        decor.setDefaultOffset (24);
        decor.setItemPadding (8);
        recyclerView.addItemDecoration (decor);
        recyclerView.setHasFixedSize (true);

        chartsAdapter = new ChartsAdapter (this,
                ((HostView) getActivity ())::fetchChartData);

        recyclerView.setAdapter (chartsAdapter);

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

        presenter.attachView (this);

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showCharts (List<Chart> charts) {

        if ( charts.isEmpty () ) {
            chartsAdapter.setError (getString (R.string.no_charts_err));
        } else {
            chartsAdapter.setItems (charts);
        }
    }

    @Override
    public void onChartClick (Chart chart) {
        if ( chart.id == CHART_POPULAR ||
                chart.id == CHART_LATEST ||
                chart.id == CHART_TOP_RATED ) {
            ((HostView) getActivity ()).openChartMovieList (chart);
        }
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
        chartsAdapter.setError (message);
    }
}
