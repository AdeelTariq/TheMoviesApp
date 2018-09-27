package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class DefaultListDecoration extends DefaultItemDecoration {

    private int spanCount;

    public DefaultListDecoration (Context context, int orientation, int spanCount) {
        super (context, orientation);
        this.spanCount = spanCount;
    }

    private int itemPadding;

    public void setItemPadding (int itemPadding) {
        this.itemPadding = itemPadding;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.left = (int) dpToPx (defaultOffset);
        outRect.right = (int) dpToPx (defaultOffset);
        outRect.bottom = (int) dpToPx (itemPadding);

        if ( spanCount == 2 ) {

            if ( parent.getChildLayoutPosition (view) % spanCount == 0 )
                outRect.right = (int) dpToPx (itemPadding);

            if ( parent.getChildLayoutPosition (view) % spanCount == 1 )
                outRect.left = (int) dpToPx (itemPadding);
        }

        if ( spanCount == 3 ) {

            if ( parent.getChildLayoutPosition (view) % spanCount == 0 )
                outRect.right = 0;

            if ( parent.getChildLayoutPosition (view) % spanCount == 2 )
                outRect.left = 0;
        }
    }
}
