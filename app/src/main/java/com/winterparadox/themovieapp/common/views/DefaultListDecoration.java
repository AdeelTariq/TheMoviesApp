package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class DefaultListDecoration extends DefaultItemDecoration {

    public DefaultListDecoration (Context context, int orientation) {
        super (context, orientation);
    }

    int itemPadding;

    public void setItemPadding (int itemPadding) {
        this.itemPadding = itemPadding;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.left = (int) dpToPx (defaultOffset);
        outRect.right = (int) dpToPx (defaultOffset);
        outRect.top = (int) dpToPx (itemPadding);
    }
}
