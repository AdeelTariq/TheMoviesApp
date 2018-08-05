package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.winterparadox.themovieapp.R;

/**
 * Frame layout that has rounded corners (it clips content too).
 *
 * @author Anton Chekulaev
 */
public class RoundCornerFrameLayout extends FrameLayout {

    private final Path stencilPath = new Path ();
    private float cornerRadius = 0;

    public RoundCornerFrameLayout (Context context) {
        this (context, null);
    }

    public RoundCornerFrameLayout (Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public RoundCornerFrameLayout (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);

        TypedArray attrArray = context.obtainStyledAttributes (attrs, R.styleable
                .RoundCornerFrameLayout, 0, 0);
        try {
            cornerRadius = attrArray.getDimensionPixelSize (R.styleable
                    .RoundCornerFrameLayout_corner_radius, 0);
        } finally {
            attrArray.recycle ();
        }
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);

        // compute the path
        stencilPath.reset ();
        stencilPath.addRoundRect (new RectF (0, 0, w, h),
                new float[]{cornerRadius, cornerRadius,        // pairs: top-left,
                        cornerRadius, cornerRadius,             // top-right,
                        0, 0, 0, 0},                               // bottom-right, bottom-left
                Path.Direction.CW);
        stencilPath.close ();

    }

    @Override
    protected void dispatchDraw (@NonNull Canvas canvas) {
        int save = canvas.save ();
        canvas.clipPath (stencilPath);
        super.dispatchDraw (canvas);
        canvas.restoreToCount (save);
    }
}