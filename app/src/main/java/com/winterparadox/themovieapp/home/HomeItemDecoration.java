package com.winterparadox.themovieapp.home;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.winterparadox.themovieapp.common.views.DefaultItemDecoration;

import static com.winterparadox.themovieapp.common.views.Utils.dpToPx;

public class HomeItemDecoration extends DefaultItemDecoration {

    public HomeItemDecoration (Context context, int orientation) {
        super (context, orientation);
    }

    int middleHorizontalPadding;
    int verticalOffset;

    public void setMiddleHorizontalPadding (int middleHorizontalPadding) {
        this.middleHorizontalPadding = middleHorizontalPadding;
    }

    public void setVerticalOffset (int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.left = (int) dpToPx (defaultOffset);
        outRect.right = (int) dpToPx (defaultOffset);
        outRect.bottom = (int) dpToPx (verticalOffset);

        if ( parent.getChildAdapterPosition (view) % 5 == 0 ) {
            outRect.bottom = 0;
        }

        if ( parent.getChildAdapterPosition (view) % 5 == 2 ) {
            outRect.right = 0;
        }

        if ( parent.getChildAdapterPosition (view) % 5 == 3 ) {
            outRect.right = (int) dpToPx (middleHorizontalPadding);
            outRect.left = (int) dpToPx (middleHorizontalPadding);
        }

        if ( parent.getChildAdapterPosition (view) % 5 == 4 ) {
            outRect.left = 0;
        }
    }
}
