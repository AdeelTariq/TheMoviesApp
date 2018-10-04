package com.winterparadox.themovieapp.hostAndSearch;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.winterparadox.themovieapp.App;
import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.arch.Navigator;
import com.winterparadox.themovieapp.charts.ChartsFragment;
import com.winterparadox.themovieapp.charts.chartMovieList.ChartMovieListFragment;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.common.beans.Chart;
import com.winterparadox.themovieapp.common.beans.Movie;
import com.winterparadox.themovieapp.common.beans.Person;
import com.winterparadox.themovieapp.common.beans.UserList;
import com.winterparadox.themovieapp.createList.CreateListDialogFragment;
import com.winterparadox.themovieapp.favorites.FavoritesFragment;
import com.winterparadox.themovieapp.home.HomeFragment;
import com.winterparadox.themovieapp.hostAndSearch.searchResults.SearchResultFragment;
import com.winterparadox.themovieapp.movieDetails.MovieDetailsFragment;
import com.winterparadox.themovieapp.movieDetails.addToList.UserListDialogFragment;
import com.winterparadox.themovieapp.personDetails.PersonDetailsFragment;
import com.winterparadox.themovieapp.recentlyViewed.RecentlyViewedFragment;
import com.winterparadox.themovieapp.userLists.UserListsFragment;
import com.winterparadox.themovieapp.userLists.renameList.RenameListDialogFragment;
import com.winterparadox.themovieapp.userLists.userMovieList.UserMovieListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HostActivity extends AppCompatActivity implements HostView, Navigator,
        SuggestionAdapter.ClickListener {

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
    private RecyclerView rvSuggestions;
    private SuggestionAdapter suggestionAdapter;
    private View searchProgressbar;

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

        ViewGroup layoutChild = (ViewGroup) menuLayout.getChildAt (0);
        for ( int i = 0; i < layoutChild.getChildCount (); i++ ) {
            layoutChild.getChildAt (i).setOnClickListener (this::onMenuClick);
        }

        View seachLayout = getLayoutInflater ().inflate (R.layout.layout_backdrop_search,
                null, false);
        rvSuggestions = seachLayout.findViewById (R.id.rvSuggestions);
        searchProgressbar = seachLayout.findViewById (R.id.searchProgress);
        rvSuggestions.setLayoutManager (new LinearLayoutManager (this));
        suggestionAdapter = new SuggestionAdapter (this);
        rvSuggestions.setAdapter (suggestionAdapter);

        backDropNavigationListener = new BackDropNavigationListener (this,
                toolbar.getChildAt (1),
                frontSheet, backdropHolder, menuLayout, seachLayout,
                new DecelerateInterpolator (),
                getDrawable (R.drawable.ic_menu),
                getDrawable (R.drawable.ic_close));

        setSupportActionBar (toolbar);
        toolbar.setNavigationOnClickListener (backDropNavigationListener);
        frontSheet.setOnClickListener ((v) -> closeBackdrop ());

        presenter.attachView (this, this);
        presenter.fetchChartData ();

        if ( savedInstanceState == null ) {
            getSupportFragmentManager ().beginTransaction ()
                    .add (R.id.container, new HomeFragment (), "home").commit ();
        }

    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        presenter.detachView ();
    }

    private void closeBackdrop () {
        if ( backDropNavigationListener.isBackdropShown () ) {
            if ( searchItem.isActionViewExpanded () ) {
                searchItem.collapseActionView ();
            } else {
                backDropNavigationListener.toggle ();
            }
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
            public boolean onQueryTextChange (String query) {
                presenter.getSuggestions (query);
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
        presenter.search (query);
    }

    @Override
    public void showSuggestions (List<Movie> movies) {
        suggestionAdapter.setItems (movies);
    }

    @Override
    public void clearSuggestions () {
        suggestionAdapter.clear ();
    }

    @Override
    public void onMovieSuggestionClick (Movie movie) {
        closeBackdrop ();
        presenter.onMovieSuggestionClicked (movie);
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
     * @param v view
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

            case R.id.action_about:
                presenter.onAboutClicked ();
                break;
        }
    }

    @Override
    public void openMovie (Movie movie, Object view) {
        View element = ((View) view);
        openFragment (this,
                MovieDetailsFragment.instance (movie),
                movie.title, element);
    }

    @Override
    public void openPerson (Person person, Object view) {
        View element = ((View) view);
        openFragment (this,
                PersonDetailsFragment.instance (person),
                person.name, element);
    }

    @Override
    public void openFavorites () {
        openFragment (this, new FavoritesFragment (),
                "favorites", null);
    }

    @Override
    public void openRecentlyViewed () {
        openFragment (this, new RecentlyViewedFragment (),
                "recents", null);
    }

    @Override
    public void openCharts () {
        openFragment (this, new ChartsFragment (),
                "charts", null);
    }

    @Override
    public void openMyLists () {
        openFragment (this, new UserListsFragment (),
                "myLists", null);
    }

    @Override
    public void openMyList (UserList list) {
        openFragment (this, UserMovieListFragment.instance (list),
                list.name, null);
    }

    @Override
    public void openListSelector (ArrayList<UserList> userLists, long movieId) {
        UserListDialogFragment dialog = UserListDialogFragment.instance (movieId, userLists);
        dialog.show (getSupportFragmentManager (), "listSelector");
    }

    @Override
    public void openChartMovieList (Chart chart) {
        openFragment (this,
                ChartMovieListFragment.instance (chart),
                chart.name, null);
    }

    @Override
    public void openCreateList (long movieId) {
        CreateListDialogFragment dialog = CreateListDialogFragment.instance (movieId);
        dialog.show (getSupportFragmentManager (), "createList");
    }

    @Override
    public void openRenameList (UserList list) {
        RenameListDialogFragment dialog = RenameListDialogFragment.instance (list);
        dialog.show (getSupportFragmentManager (), "renameList");
    }

    @Override
    public void openSearch () {
        searchItem.expandActionView ();
    }

    @Override
    public void openSearchResults (String query, List<Movie> movies) {
        closeBackdrop ();
        openFragment (this,
                SearchResultFragment.instance (query, new ArrayList<> (movies)),
                "search " + query, null);
    }

    @Override
    public void openAbout () {
        LibsSupportFragment fragment = new LibsBuilder ()
                .withAboutIconShown (true)
                .withAboutVersionShown (true)
                .withAboutDescription (getString (R.string.about_text))
                .supportFragment ();

        openFragment (this, fragment, "about", null);
    }

    public static void openFragment (AppCompatActivity activity,
                                     Fragment fragment, String tag,
                                     View sharedElement) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager ();

        FragmentTransaction ftx = fragmentManager.beginTransaction ();

        if ( sharedElement != null ) {
            ftx.addSharedElement (sharedElement, sharedElement.getTransitionName ());
        }


        ftx.replace (R.id.container, fragment, tag);

        ftx.addToBackStack (tag);

        ftx.commit ();
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
        if ( searchProgressbar != null ) {
            searchProgressbar.setVisibility (View.VISIBLE);
        }
    }

    @Override
    public void hideProgress () {
        if ( searchProgressbar != null ) {
            searchProgressbar.setVisibility (View.GONE);
        }
    }

    @Override
    public void showMessage (String message) {

    }

    @Override
    public void showError (String message) {
        Toast.makeText (this, message, Toast.LENGTH_LONG).show ();
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
