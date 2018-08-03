package com.winterparadox.themovieapp.common.views;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {

    public static float pxToDp (float px) {
        DisplayMetrics metrics = Resources.getSystem ().getDisplayMetrics ();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round (dp);
    }

    public static float dpToPx (float dp) {
        DisplayMetrics metrics = Resources.getSystem ().getDisplayMetrics ();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round (px);
    }
}
