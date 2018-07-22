package com.winterparadox.themovieapp.search;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.winterparadox.themovieapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.frontSheet) FrameLayout frontSheet;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);

        // setup toolbar
        NavigationIconClickListener navigationIconClickListener;
        navigationIconClickListener = new NavigationIconClickListener (this, frontSheet,
                new DecelerateInterpolator (),
                getDrawable (R.drawable.ic_menu),
                getDrawable (R.drawable.ic_close));

        setSupportActionBar (toolbar);
        toolbar.setNavigationOnClickListener (navigationIconClickListener);
        frontSheet.setOnClickListener ((v) -> {
            if ( navigationIconClickListener.isBackdropShown () ) {
                navigationIconClickListener.onClick (toolbar.getChildAt (1));
            }
        });
    }
}
