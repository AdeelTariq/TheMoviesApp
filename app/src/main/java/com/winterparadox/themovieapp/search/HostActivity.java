package com.winterparadox.themovieapp.search;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.charts.ChartsFragment;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListFragment;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.favorites.FavoritesFragment;
import com.winterparadox.themovieapp.home.HomeFragment;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsFragment;
import com.winterparadox.themovieapp.personDetails.PersonDetailsFragment;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostActivity extends AppCompatActivity implements HostView, Navigator {

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

        presenter.attachView (this, this);

        if ( savedInstanceState == null ) {
            getSupportFragmentManager ().beginTransaction ()
                    .add (R.id.container, new HomeFragment (), "home").commit ();
        }

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
                presenter.onRecentlyViewedClicked ();
                break;

            case R.id.action_favorite:
                presenter.onFavoritesClicked ();
                break;

            case R.id.action_lists:
                presenter.onMyListsClicked ();
                break;

            case R.id.action_charts:
                presenter.onChartsClicked ();
                break;
        }
    }

    @Override
    public void openMovie (Movie movie, Object view) {
        View element = ((View) view);
        resurfaceFragment (this,
                MovieDetailsFragment.instance (movie),
                movie.title, element);
    }

    @Override
    public void openPerson (Person person, Object view) {
        View element = ((View) view);
        resurfaceFragment (this,
                PersonDetailsFragment.instance (person),
                person.name, element);
    }

    @Override
    public void openFavorites () {
        resurfaceFragment (this, new FavoritesFragment (),
                "favorites", null);
    }

    @Override
    public void openRecentlyViewed () {
        resurfaceFragment (this, new RecentlyViewedFragment (),
                "recents", null);
    }

    @Override
    public void openCharts () {
        resurfaceFragment (this, new ChartsFragment (),
                "charts", null);
    }

    @Override
    public void openMyLists () {
        //
    }

    @Override
    public void openChartMovieList (Chart chart) {
        resurfaceFragment (this,
                ChartMovieListFragment.instance (chart),
                chart.name, null);
    }

    public static void resurfaceFragment (AppCompatActivity activity,
                                          Fragment fragment, String tag,
                                          View sharedElement) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager ();

        boolean fragmentPopped = fragmentManager
                .popBackStackImmediate (tag, 0);

        List<Fragment> fragments = fragmentManager.getFragments ();

        if ( !fragmentPopped && fragmentManager.findFragmentByTag (tag) == null ) {

            FragmentTransaction ftx = fragmentManager.beginTransaction ();

            if ( sharedElement != null ) {
                ftx.addSharedElement (sharedElement, sharedElement.getTransitionName ());
            }


            ftx.replace (R.id.container, fragment, tag);

            ftx.addToBackStack (tag);

            ftx.commit ();
        }
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

    @Override
    public List<String> getDefaultCharts () {
        List<String> list = new ArrayList<> ();
        list.add (getString (R.string.popular));
        list.add (getString (R.string.latest));
        list.add (getString (R.string.top_rated));
        return list;
    }
}
