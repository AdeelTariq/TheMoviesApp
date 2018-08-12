package com.winterparadox.themovieapp.recentlyViewed;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.winterparadox.themovieapp.common.views.DefaultItemDecoration;

import static com.winterparadox.themovieapp.common.UiUtils.dpToPx;

public class RecentMovieDecoration extends DefaultItemDecoration {

    private final int spanCount;

    RecentMovieDecoration (Context context, int orientation, int spanCount) {
        super (context, orientation);
        this.spanCount = spanCount;
    }

    private int itemPadding;

    void setItemPadding (int itemPadding) {
        this.itemPadding = itemPadding;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent,
                                RecyclerView.State state) {
        outRect.left = (int) dpToPx (defaultOffset);
        outRect.right = (int) dpToPx (defaultOffset);
        outRect.bottom = (int) dpToPx (itemPadding);


        if ( parent.getChildAdapterPosition (view) < spanCount ) {
            outRect.top = (int) dpToPx (itemPadding);
        }

        if ( spanCount == 3 ) {

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

        } else if ( spanCount == 4 ) {

            int i = parent.getChildAdapterPosition (view) % 4;
            if ( i == 0 ) {
                outRect.right = 0;
            }

            if ( i == 1 ) {
                outRect.right = (int) dpToPx (itemPadding);
                outRect.left = (int) dpToPx (itemPadding * 2);
            }

            if ( i == 2 ) {
                outRect.right = (int) dpToPx (itemPadding * 2);
                outRect.left = (int) dpToPx (itemPadding);
            }

            if ( i == 3 ) {
                outRect.left = 0;
            }

        }
    }
}
