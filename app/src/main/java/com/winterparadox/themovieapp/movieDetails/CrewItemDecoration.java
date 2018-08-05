package com.winterparadox.themovieapp.movieDetails;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.winterparadox.themovieapp.common.views.DefaultItemDecoration;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class CrewItemDecoration extends DefaultItemDecoration {

    private float horizontal = 24;

    public CrewItemDecoration (Context context, int orientation) {
        super (context, orientation);
    }

    public void setHorizontal (float lastItemOffset) {
        this.horizontal = lastItemOffset;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.right = (int) dpToPx (horizontal);
        outRect.bottom = (int) dpToPx (defaultOffset);
    }
}
