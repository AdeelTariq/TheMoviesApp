package com.winterparadox.themovieapp.charts;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
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
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.ArrayList;
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

        chartsAdapter = new ChartsAdapter (this,
                presenter::fetchChartData);


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

            recyclerView.setGridChildCount (gridLayoutManager.getSpanCount ());
            recyclerView.setDemoLayoutManager (ShimmerRecyclerView.LayoutMangerType.GRID);
            recyclerView.setGridChildCount (gridLayoutManager.getSpanCount ());
            recyclerView.setItemViewCacheSize (20);
            recyclerView.setDrawingCacheEnabled (true);

            presenter.attachView (this, ((Navigator) getActivity ()));
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
    public void showCharts (List<Chart> charts, int firstVisibleItem) {

        ChangeBounds transition = new ChangeBounds ();
        Fade transition2 = new Fade ();
        TransitionSet transitionSet = new TransitionSet ().addTransition (transition)
                .addTransition (transition2);
        transitionSet.setInterpolator (new AccelerateInterpolator (1));
        transitionSet.setDuration (300);
        TransitionManager.beginDelayedTransition ((ViewGroup) tvHeader.getParent (), transitionSet);

        if ( charts.isEmpty () ) {
            chartsAdapter.setError (getString (R.string.no_charts_err));
        } else {
            chartsAdapter.setItems (charts);
        }

        recyclerView.post (() -> recyclerView.getLayoutManager ()
                .scrollToPosition (firstVisibleItem));
    }

    @Override
    public void onChartClick (Chart chart) {
        presenter.onChartClicked (chart);
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

    @Override
    public List<String> getDefaultCharts () {
        List<String> list = new ArrayList<> ();
        list.add (getString (R.string.popular));
        list.add (getString (R.string.latest));
        list.add (getString (R.string.top_rated));
        return list;
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

            if ( chartsAdapter.getItemCount () > 4 ) {
                return;
            }

            getActivity ().runOnUiThread (presenter::fetchChartData);
        }
    };

}
