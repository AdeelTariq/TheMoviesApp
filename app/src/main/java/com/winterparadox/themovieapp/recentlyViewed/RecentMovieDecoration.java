package com.winterparadox.themovieapp.recentlyViewed;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.winterparadox.themovieapp.common.views.DefaultItemDecoration;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class RecentMovieDecoration extends DefaultItemDecoration {

    public RecentMovieDecoration (Context context, int orientation) {
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
        outRect.bottom = (int) dpToPx (itemPadding);

        if ( parent.getChildAdapterPosition (view) == 0 ) {
            outRect.top = (int) dpToPx (itemPadding);
        }
        if ( parent.getChildAdapterPosition (view) == 1 ) {
            outRect.top = (int) dpToPx (itemPadding);
        }
        if ( parent.getChildAdapterPosition (view) == 2 ) {
            outRect.top = (int) dpToPx (itemPadding);
        }

        int i = parent.getChildAdapterPosition (view) % 3;
        if ( i == 1 ) {
            outRect.right = (int) dpToPx (itemPadding);
            outRect.left = (int) dpToPx (itemPadding);
        }

        if ( i == 0 ) {
            outRect.right = 0;
        }

        if ( i == 2 ) {
            outRect.left = 0;
        }
    }
}
