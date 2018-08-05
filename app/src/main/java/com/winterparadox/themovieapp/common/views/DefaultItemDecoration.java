package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class DefaultItemDecoration extends DividerItemDecoration {

    protected int defaultOffset = 16;

    public DefaultItemDecoration (Context context, int orientation) {
        super (context, orientation);
        setDrawable (new ColorDrawable (0x00000000));
    }

    public void setDefaultOffset (int defaultOffset) {
        this.defaultOffset = defaultOffset;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.bottom = (int) dpToPx (defaultOffset);
    }
}
