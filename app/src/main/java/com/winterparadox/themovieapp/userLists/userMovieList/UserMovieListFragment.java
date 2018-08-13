package com.winterparadox.themovieapp.userLists.userMovieList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserMovieListFragment extends Fragment implements UserMovieListView,
        UserMovieListAdapter.ClickListener {

    private static final String LIST = "list";
    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;
    private UserList list;
    private UserMovieListAdapter movieListAdapter;

    @Inject UserMovieListPresenter presenter;

    public static UserMovieListFragment instance (UserList list) {
        UserMovieListFragment userMovieListFragment = new UserMovieListFragment ();
        Bundle args = new Bundle ();
        args.putSerializable (LIST, list);
        userMovieListFragment.setArguments (args);
        return userMovieListFragment;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        list = (UserList) getArguments ().getSerializable (LIST);

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (list.name);

        GridLayoutManager gridLayoutManager;

        if ( getActivity ().getResources ().getConfiguration ().orientation == Configuration
                .ORIENTATION_PORTRAIT ) {
            gridLayoutManager = new GridLayoutManager (getActivity (), 1);

        } else {
            gridLayoutManager = new GridLayoutManager (getActivity (), 2);
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

        movieListAdapter = new UserMovieListAdapter (this, presenter::onDiscoverClick);

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

        presenter.attachView (this, list, (Navigator) getActivity ());

        return view;
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        if ( recyclerView.getLayoutManager () != null ) {
            int visibleItemPosition = ((GridLayoutManager) recyclerView
                    .getLayoutManager ()).findLastCompletelyVisibleItemPosition ();
            presenter.saveState (visibleItemPosition);
        }
        presenter.detachView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies) {
        movieListAdapter.setItems (movies);

        if ( movies.isEmpty () ) {
            movieListAdapter.setError (getString (R.string.no_movies_user_list));
        }
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
    public void showMessage (String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

    @Override
    public void showError (String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

}
