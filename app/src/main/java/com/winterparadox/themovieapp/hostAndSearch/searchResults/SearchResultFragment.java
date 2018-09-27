package com.winterparadox.themovieapp.hostAndSearch.searchResults;

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
import com.winterparadox.themovieapp.common.views.EndlessRecyclerViewScrollListener;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultFragment extends Fragment implements SearchResultView,
        SearchResultAdapter.ClickListener {

    private static final String RESULT = "result";
    private static final String QUERY = "query";

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;

    Unbinder unbinder;

    private ArrayList<Movie> initialResult;
    private String query;

    private SearchResultAdapter movieListAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Inject SearchResultPresenter presenter;

    public static SearchResultFragment instance (String query, ArrayList<Movie> initialResult) {
        SearchResultFragment searchResultFragment = new SearchResultFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (RESULT, initialResult);
        args.putString (QUERY, query);
        searchResultFragment.setArguments (args);
        return searchResultFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        //noinspection unchecked
        initialResult = (ArrayList<Movie>) getArguments ().getSerializable (RESULT);
        query = getArguments ().getString (QUERY);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (String.format (getString (R.string.results_for), query));

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

        recyclerView.setItemViewCacheSize (20);
        recyclerView.setDrawingCacheEnabled (true);

        recyclerView.setDemoChildCount (10);
        recyclerView.setDemoLayoutManager (ShimmerRecyclerView.LayoutMangerType.GRID);
        recyclerView.setDemoLayoutReference (R.layout.layout_movie_list_shimmer_item);
        recyclerView.setGridChildCount (gridLayoutManager.getSpanCount ());

        movieListAdapter = new SearchResultAdapter (this, presenter::onRetryClicked);

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

        scrollListener = new EndlessRecyclerViewScrollListener (gridLayoutManager) {
            @Override
            public void onLoadMore (int page, int totalItemsCount, RecyclerView view) {
                presenter.fetchDataPage (page);
            }
        };
        recyclerView.addOnScrollListener (scrollListener);

        if ( initialResult.isEmpty () ) {
            movieListAdapter.setError (getString (R.string.not_found));
        } else {
            movieListAdapter.setItems (initialResult);
        }

        presenter.attachView (this, query, (Navigator) getActivity ());


        return view;
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        if ( recyclerView.getLayoutManager () != null ) {
            int visibleItemPosition = ((GridLayoutManager) recyclerView
                    .getLayoutManager ()).findLastCompletelyVisibleItemPosition ();
            List<Object> items = movieListAdapter.getItems ();
            presenter.saveState (visibleItemPosition, items);
        }
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies) {

        ChangeBounds transition = new ChangeBounds ();
        Fade transition2 = new Fade ();
        TransitionSet transitionSet = new TransitionSet ().addTransition (transition)
                .addTransition (transition2);
        transitionSet.setInterpolator (new AccelerateInterpolator (1));
        transitionSet.setDuration (300);
        TransitionManager.beginDelayedTransition ((ViewGroup) tvHeader.getParent (), transitionSet);

        movieListAdapter.setItems (movies);
    }

    @Override
    public void restoreMovies (int visiblePos, List items, int page) {
        if ( movieListAdapter != null ) {
            movieListAdapter.setItems (items);
        }
        if ( recyclerView != null ) {
            recyclerView.getLayoutManager ().scrollToPosition (visiblePos);
        }
        if ( scrollListener != null ) {
            scrollListener.setState (page);
        }
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

}
