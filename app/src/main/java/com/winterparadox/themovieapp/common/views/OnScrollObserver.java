package com.winterparadox.themovieapp.common.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class OnScrollObserver extends RecyclerView.OnScrollListener {

    public abstract void onScrolling ();

    public abstract void onScrollToTop ();

    @Override
    public void onScrolled (@NonNull RecyclerView recyclerView, int dx, int dy) {
        int scrollY = recyclerView.computeVerticalScrollOffset ();
        if ( scrollY > 0 ) {
            onScrolling ();
        } else {
            onScrollToTop ();
        }
    }
}