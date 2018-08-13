package com.winterparadox.themovieapp.userLists;

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
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.common.views.DefaultListDecoration;
import com.winterparadox.themovieapp.common.views.OnScrollObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserListsFragment extends Fragment implements UserListsView, UserListsAdapter
        .ClickListener {

    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.recyclerView) ShimmerRecyclerView recyclerView;
    @BindView(R.id.scrollIndicator) ImageView scrollIndicator;
    Unbinder unbinder;
    private UserListsAdapter userListsAdapter;

    @Inject UserListsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        ((App) getActivity ().getApplication ()).getAppComponent ().inject (this);

        View view = inflater.inflate (R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind (this, view);

        tvHeader.setText (R.string.my_lists);

        userListsAdapter = new UserListsAdapter (this);


        recyclerView.setAdapter (userListsAdapter);

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
    public void showCharts (List<UserList> userLists, int firstVisibleItem) {

        userListsAdapter.setItems (userLists);

        recyclerView.post (() -> recyclerView.getLayoutManager ()
                .scrollToPosition (firstVisibleItem));
    }

    @Override
    public void onUserListClick (UserList list) {
        presenter.onUserListClicked (list);
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
        Toast.makeText (getActivity (), message, Toast.LENGTH_LONG).show ();
    }

    @Override
    public List<String> getDefaultLists () {
        List<String> list = new ArrayList<> ();
        list.add (getString (R.string.watchlist));
        list.add (getString (R.string.watched));
        list.add (getString (R.string.collection));
        return list;
    }
}
