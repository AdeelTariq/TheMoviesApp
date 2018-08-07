package com.winterparadox.themovieapp.movieDetails;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.winterparadox.themovieapp.common.views.DefaultItemDecoration;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class CastItemDecoration extends DefaultItemDecoration {

    private float lastItemEndOffset = 24;

    CastItemDecoration (Context context, int orientation) {
        super (context, orientation);
    }

    void setLastItemEndOffset (float lastItemOffset) {
        this.lastItemEndOffset = lastItemOffset;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.right = (int) dpToPx (defaultOffset);

        if ( parent.getChildAdapterPosition (view) == parent.getAdapter ().getItemCount () - 1 ) {
            outRect.right = (int) dpToPx (lastItemEndOffset);
        }
    }
}
