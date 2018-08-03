package com.winterparadox.themovieapp.search;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.NetworkUtils;
import com.winterparadox.themovieapp.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.frontSheet) LinearLayoutCompat frontSheet;
    @BindView(R.id.backdropHolder) LinearLayout backdropHolder;
    @BindView(R.id.container) FrameLayout container;
    private BackDropNavigationListener backDropNavigationListener;
    private MenuItem offlineModeItem;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);

        LayoutTransition transition = new LayoutTransition ();
        transition.enableTransitionType (LayoutTransition.CHANGING);
        backdropHolder.setLayoutTransition (transition);

        // setup toolbar
        backDropNavigationListener = new BackDropNavigationListener (this, toolbar.getChildAt (1),
                frontSheet, backdropHolder,
                getLayoutInflater ().inflate (R.layout.layout_backdrop_menu, null, false),
                getLayoutInflater ().inflate (R.layout.layout_backdrop_search, null, false),
                new DecelerateInterpolator (),
                getDrawable (R.drawable.ic_menu),
                getDrawable (R.drawable.ic_close));

        setSupportActionBar (toolbar);
        toolbar.setNavigationOnClickListener (backDropNavigationListener);
        frontSheet.setOnClickListener ((v) -> {
            if ( backDropNavigationListener.isBackdropShown () ) {
                backDropNavigationListener.toggle ();
            }
        });

        getSupportFragmentManager ().beginTransaction ()
                .add (R.id.container, new HomeFragment (), "home").commit ();

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater ().inflate (R.menu.menu_main_search, menu);

        offlineModeItem = menu.findItem (R.id.action_offline);

        MenuItem searchItem = menu.findItem (R.id.action_search);
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
            if ( !NetworkUtils.isConnected (MainActivity.this) ) {
                return;
            }
            runOnUiThread (() -> offlineModeItem.setVisible (false));
        }

        @Override
        public void onLost (Network network) {
            if ( offlineModeItem == null ) {
                return;
            }
            if ( NetworkUtils.isConnected (MainActivity.this) ) {
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
}
