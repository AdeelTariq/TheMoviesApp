package com.winterparadox.themovieapp.common.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.BitmapTransformation;

public class GradientColorFilterTransformation extends BitmapTransformation {

    @Override
    protected Bitmap transform (@NonNull Context context, @NonNull BitmapPool pool, @NonNull
            Bitmap originalBitmap, int outWidth, int outHeight) {
        int width = originalBitmap.getWidth ();
        int height = originalBitmap.getHeight ();
        Bitmap updatedBitmap = Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (updatedBitmap);

        canvas.drawBitmap (originalBitmap, 0, 0, null);

        Paint paint = new Paint ();
        LinearGradient shader = new LinearGradient (0, 0, 0, height,
                0x77FFFFFF, 0x77000000, Shader.TileMode.CLAMP);
        paint.setShader (shader);
        paint.setXfermode (new PorterDuffXfermode (PorterDuff.Mode.DARKEN));
        canvas.drawRect (0, 0, width, height, paint);
        return updatedBitmap;

    }

    @Override
    public void updateDiskCacheKey (@NonNull MessageDigest messageDigest) {

    }

    @Override
    public boolean equals (Object o) {
        return false;
    }

    @Override
    public int hashCode () {
        return 0;
    }
}
