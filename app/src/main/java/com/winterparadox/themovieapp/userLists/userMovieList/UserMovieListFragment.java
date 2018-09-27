package com.winterparadox.themovieapp.userLists.userMovieList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;
import com.woxthebox.draglistview.DragListView;

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

public class UserMovieListFragment extends Fragment implements UserMovieListView,
        UserMovieListAdapter.ClickListener, DragListView.DragListListener {

    private static final String LIST = "list";
    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) DragListView recyclerView;
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
        View view = inflater.inflate (R.layout.fragment_draggable_recyclerview, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (list.name);

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
        recyclerView.getRecyclerView ().addItemDecoration (decor);
        recyclerView.setCanDragHorizontally (true);

        recyclerView.getRecyclerView ().setItemViewCacheSize (20);
        recyclerView.setDrawingCacheEnabled (true);

        movieListAdapter = new UserMovieListAdapter (this, presenter::onDiscoverClick);

        recyclerView.setAdapter (movieListAdapter, false);

        recyclerView.getRecyclerView ().addOnScrollListener (new OnScrollObserver () {
            @Override
            public void onScrolling () {
                scrollIndicator.setVisibility (View.VISIBLE);
            }

            @Override
            public void onScrollToTop () {
                scrollIndicator.setVisibility (View.GONE);
            }
        });

        // dont allow error item to be dragged
        recyclerView.setDragListCallback (new DragListView.DragListCallback () {
            @Override
            public boolean canDragItemAtPosition (int dragPosition) {
                return movieListAdapter.getItemList ().get (dragPosition) instanceof Movie;
            }

            @Override
            public boolean canDropItemAtPosition (int dropPosition) {
                return true;
            }
        });
        recyclerView.setDragListListener (this);

        presenter.attachView (this, list, (Navigator) getActivity ());

        return view;
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        if ( recyclerView.getRecyclerView ().getLayoutManager () != null ) {
            int visibleItemPosition = ((GridLayoutManager) recyclerView.getRecyclerView ()
                    .getLayoutManager ()).findLastCompletelyVisibleItemPosition ();
            presenter.saveState (visibleItemPosition);
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

        movieListAdapter.setMovies (movies);

        if ( movies.isEmpty () ) {
            movieListAdapter.setError (getString (R.string.no_movies_user_list));
        }
    }

    @Override
    public void onMovieClick (Movie movie, View element) {
        presenter.onMovieClicked (movie, element);
    }

    @Override
    public void onDeleteClick (Movie movie) {
        presenter.deleteMovie (movie);
    }

    @Override
    public void onItemDragEnded (int fromPosition, int toPosition) {
        List<Object> itemList = movieListAdapter.getItemList ();

        ArrayList<Object> movies = new ArrayList<> (itemList);
        presenter.saveListOrder (movies);
    }

    @Override
    public void onItemDragStarted (int position) {

    }

    @Override
    public void onItemDragging (int itemPosition, float x, float y) {

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
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

}
