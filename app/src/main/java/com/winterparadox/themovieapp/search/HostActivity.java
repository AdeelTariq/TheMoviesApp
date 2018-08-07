package com.winterparadox.themovieapp.search;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.favorites.FavoritesFragment;
import com.winterparadox.themovieapp.home.HomeFragment;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsFragment;
import com.winterparadox.themovieapp.personDetails.PersonDetailsFragment;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostActivity extends AppCompatActivity implements HostView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.frontSheet) ViewGroup frontSheet;
    @BindView(R.id.backdropHolder) LinearLayout backdropHolder;
    @BindView(R.id.container) FrameLayout container;
    MaterialButton actionHistory;
    MaterialButton actionFavorite;

    private BackDropNavigationListener backDropNavigationListener;
    private MenuItem offlineModeItem, searchItem;

    @Inject HostPresenter presenter;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        ((App) getApplication ()).getAppComponent ().inject (this);

        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);

        LayoutTransition transition = new LayoutTransition ();
        transition.enableTransitionType (LayoutTransition.CHANGING);
        backdropHolder.setLayoutTransition (transition);

        // setup toolbar
        ViewGroup menuLayout = (ViewGroup) getLayoutInflater ()
                .inflate (R.layout.layout_backdrop_menu, null, false);
        actionFavorite = menuLayout.findViewById (R.id.action_favorite);
        actionHistory = menuLayout.findViewById (R.id.action_history);

        for ( int i = 0; i < menuLayout.getChildCount (); i++ ) {
            menuLayout.getChildAt (i).setOnClickListener (this::onMenuClick);
        }

        backDropNavigationListener = new BackDropNavigationListener (this, toolbar.getChildAt (1),
                frontSheet, backdropHolder,
                menuLayout,
                getLayoutInflater ().inflate (R.layout.layout_backdrop_search, null, false),
                new DecelerateInterpolator (),
                getDrawable (R.drawable.ic_menu),
                getDrawable (R.drawable.ic_close));

        setSupportActionBar (toolbar);
        toolbar.setNavigationOnClickListener (backDropNavigationListener);
        frontSheet.setOnClickListener ((v) -> {
            if ( backDropNavigationListener.isBackdropShown () ) {
                if ( searchItem.isActionViewExpanded () ) {
                    searchItem.collapseActionView ();
                } else {
                    backDropNavigationListener.toggle ();
                }
            }
        });

        presenter.attachView (this);

        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.container, new HomeFragment (), "home").commit ();

    }

    @Override
    public void onBackPressed () {
        if ( backDropNavigationListener.isBackdropShown () ) {
            if ( searchItem.isActionViewExpanded () ) {
                searchItem.collapseActionView ();
            } else {
                backDropNavigationListener.toggle ();
            }
        } else {
            super.onBackPressed ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main_search, menu);

        offlineModeItem = menu.findItem (R.id.action_offline);
        if ( !NetworkUtils.isConnected (this) ) {
            offlineModeItem.setVisible (true);
        }

        searchItem = menu.findItem (R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView ();

        searchView.setSubmitButtonEnabled (true);


        searchView.setOnSearchClickListener ((v) -> search (searchView.getQuery ().toString ()));

        searchView.setOnQueryTextListener (new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit (String s) {
                search (s);
                return false;
            }

            @Override
            public boolean onQueryTextChange (String s) {
                return false;
            }
        });

        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener () {
            @Override
            public boolean onMenuItemActionCollapse (MenuItem item) {
                backDropNavigationListener.toggle ();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand (MenuItem item) {
                backDropNavigationListener.showSearchBackDrop ();
                return true;
            }
        };

        // Assign the listener to that action item
        searchItem.setOnActionExpandListener (expandListener);

        return super.onCreateOptionsMenu (menu);
    }

    private void search (String query) {

    }

    ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback () {
        @Override
        public void onAvailable (Network network) {
            if ( offlineModeItem == null ) {
                return;
            }
            if ( !NetworkUtils.isConnected (HostActivity.this) ) {
                return;
            }
            runOnUiThread (() -> offlineModeItem.setVisible (false));
        }

        @Override
        public void onLost (Network network) {
            if ( offlineModeItem == null ) {
                return;
            }
            if ( NetworkUtils.isConnected (HostActivity.this) ) {
                return;
            }
            runOnUiThread (() -> offlineModeItem.setVisible (true));
        }
    };


    @Override
    protected void onResume () {
        super.onResume ();
        NetworkUtils.registerConnectivityCallback (this, callback);
    }

    @Override
    protected void onPause () {
        super.onPause ();
        NetworkUtils.unregisterConnectivityCallback (this, callback);
    }

    /**
     * Menu click listener
     *
     * @param v
     */
    public void onMenuClick (View v) {
        backDropNavigationListener.toggle ();
        switch ( v.getId () ) {

            case R.id.action_history:
                openRecentlyViewed ();
                break;

            case R.id.action_favorite:
                openFavorites ();
                break;

            case R.id.action_lists:
                break;

            case R.id.action_charts:
                break;
        }
    }

    @Override
    public void openMovie (Movie movie, Object view) {
        View element = ((View) view);
        getSupportFragmentManager ().beginTransaction ()
                .addSharedElement (element, element.getTransitionName ())
                .replace (R.id.container, MovieDetailsFragment.instance (movie), movie.title)
                .addToBackStack (movie.title)
                .commit ();
    }

    @Override
    public void openPerson (Person person, Object view) {
        View element = ((View) view);
        getSupportFragmentManager ().beginTransaction ()
                .addSharedElement (element, element.getTransitionName ())
                .replace (R.id.container, PersonDetailsFragment.instance (person), person.name)
                .addToBackStack (person.name)
                .commit ();
    }

    @Override
    public void openFavorites () {
        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.container, new FavoritesFragment (), "favorites")
                .addToBackStack ("favorites")
                .commit ();
    }

    @Override
    public void openRecentlyViewed () {
        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.container, new RecentlyViewedFragment (), "history")
                .addToBackStack ("history")
                .commit ();
    }

    @Override
    public void showFavoritesMenu (boolean show) {
        if ( show ) {
            actionFavorite.setVisibility (View.VISIBLE);
        } else {
            actionFavorite.setVisibility (View.GONE);
        }
    }

    @Override
    public void showRecentMenu (boolean show) {
        if ( show ) {
            actionHistory.setVisibility (View.VISIBLE);
        } else {
            actionHistory.setVisibility (View.GONE);
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

    }
}
