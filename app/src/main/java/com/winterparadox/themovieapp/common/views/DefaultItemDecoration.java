package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

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
        outRect.top = (int) dpToPx (defaultOffset);
        outRect.left = (int) dpToPx (defaultOffset);
        outRect.right = (int) dpToPx (defaultOffset);
    }
}
