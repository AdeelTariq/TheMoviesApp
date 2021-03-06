package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class DefaultHorizontalItemDecoration extends DefaultItemDecoration {

    private float lastItemEndOffset = 24;

    public DefaultHorizontalItemDecoration (Context context, int orientation) {
        super (context, orientation);
    }

    public void setLastItemEndOffset (float lastItemOffset) {
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
