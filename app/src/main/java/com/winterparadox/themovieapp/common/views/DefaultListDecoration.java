package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
        outRect.top = (int) dpToPx (itemPadding);

        if ( spanCount == 2 ) {

            if ( parent.getChildLayoutPosition (view) % 2 == 0 )
                outRect.right = (int) dpToPx (itemPadding);

            if ( parent.getChildLayoutPosition (view) % 2 == 1 )
                outRect.left = (int) dpToPx (itemPadding);
        }
    }
}
