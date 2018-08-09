package com.winterparadox.themovieapp.search;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.winterparadox.themovieapp.R;
import com.winterparadox.themovieapp.common.views.LockableScrollView;

/**
 * {@link View.OnClickListener} used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
public class BackDropNavigationListener implements View.OnClickListener {

    private final AnimatorSet animatorSet = new AnimatorSet ();
    private Context context;
    private ViewGroup sheet;
    private final LinearLayout backdropHolder;
    private final View menuLayout;
    private final View seachLayout;
    private Interpolator interpolator;
    private int height;
    private boolean backdropShown = false;
    private Drawable openIcon;
    private Drawable closeIcon;
    private View iconView;

    BackDropNavigationListener (Context context, View iconView, ViewGroup sheet, LinearLayout
            backdropHolder, View menuLayout, View seachLayout,
                                DecelerateInterpolator decelerateInterpolator,
                                Drawable drawable, Drawable drawable1) {
        this.context = context;
        this.iconView = iconView;
        this.sheet = sheet;
        this.backdropHolder = backdropHolder;
        this.menuLayout = menuLayout;
        this.seachLayout = seachLayout;
        this.interpolator = decelerateInterpolator;
        this.openIcon = drawable;
        this.closeIcon = drawable1;

        DisplayMetrics displayMetrics = new DisplayMetrics ();
        ((Activity) context).getWindowManager ().getDefaultDisplay ().getMetrics (displayMetrics);
        height = displayMetrics.heightPixels;
    }

    @Override
    public void onClick (View view) {

        if ( backdropHolder.getChildAt (0) == null ||
                backdropHolder.getChildAt (0).getId () != menuLayout.getId () ) {
            backdropHolder.removeAllViews ();
            backdropHolder.addView (menuLayout);
        }

        toggleBackDrop (view);
    }

    public void toggle () {
        toggleBackDrop (iconView);
    }

    private void toggleBackDrop (View view) {
        backdropShown = !backdropShown;

        // Cancel the existing animations
        animatorSet.removeAllListeners ();
        animatorSet.end ();
        animatorSet.cancel ();

        updateIcon (view);

        final int translateY = height -
                context.getResources ().getDimensionPixelSize (R.dimen.default_reveal_height);

        ObjectAnimator animator = ObjectAnimator.ofFloat (sheet, "translationY", backdropShown ?
                translateY : 0);
        animator.setDuration (300);
        if ( interpolator != null ) {
            animator.setInterpolator (interpolator);
        }
        animatorSet.play (animator);
        animator.start ();

        ViewGroup sheetChild = (ViewGroup) sheet.getChildAt (0);
        int childCount = sheetChild.getChildCount ();
        for ( int i = 0; i < childCount; i++ ) {
            View viewGroup = sheetChild.getChildAt (i);
            viewGroup.setClickable (!backdropShown);

            if ( viewGroup instanceof LockableScrollView ) {
                ((LockableScrollView) viewGroup).setScrollingEnabled (!backdropShown);
            }
        }
    }

    private void updateIcon (View view) {
        if ( openIcon != null && closeIcon != null ) {
            if ( !(view instanceof ImageView) ) {
                throw new IllegalArgumentException ("updateIcon() must be called on an ImageView");
            }
            if ( backdropShown ) {
                ((ImageView) view).setImageDrawable (closeIcon);
            } else {
                ((ImageView) view).setImageDrawable (openIcon);
            }
        }
    }

    public boolean isBackdropShown () {
        return backdropShown;
    }

    public void showSearchBackDrop () {
        backdropShown = true;

        backdropHolder.removeAllViews ();
        backdropHolder.addView (seachLayout);

        // Cancel the existing animations
        animatorSet.removeAllListeners ();
        animatorSet.end ();
        animatorSet.cancel ();

        updateIcon (iconView);

        final int translateY = height -
                context.getResources ().getDimensionPixelSize (R.dimen.default_reveal_height);

        ObjectAnimator animator = ObjectAnimator.ofFloat (sheet, "translationY", backdropShown ?
                translateY : 0);
        animator.setDuration (300);
        if ( interpolator != null ) {
            animator.setInterpolator (interpolator);
        }
        animatorSet.play (animator);
        animator.start ();
    }
}