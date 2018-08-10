package com.winterparadox.themovieapp.favorites;

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
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.widget.GridLayout.VERTICAL;


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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (getActivity (),
                VERTICAL, false);
        recyclerView.setLayoutManager (linearLayoutManager);
        DefaultListDecoration decor = new DefaultListDecoration (getActivity (),
                DividerItemDecoration.VERTICAL);
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


        presenter.attachView (this, (Navigator) getActivity ());

        return view;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        unbinder.unbind ();
    }

    @Override
    public void showMovies (List<Movie> movies) {
        movieAdapter.setItems (movies);
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
    }

    @Override
    public void hideProgress () {
    }

    @Override
    public void showMessage (String message) {
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

    @Override
    public void showError (String message) {

    }
}
