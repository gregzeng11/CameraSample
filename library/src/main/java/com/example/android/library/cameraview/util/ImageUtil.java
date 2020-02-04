package com.example.android.library.cameraview.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * created by zyh
 * on 2019-09-19
 */
public class ImageUtil {
    public static Bitmap decodeBitmap(String imagePath,Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int dw = displaymetrics.widthPixels;
        int dh = displaymetrics.heightPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);

        int heightRatio = (int) Math.ceil(opts.outHeight / (float) dh);
        int widthRatio = (int) Math.ceil(opts.outWidth / (float) dw);

        if (heightRatio > 1 && widthRatio > 1) {
            if (heightRatio > widthRatio) {
                opts.inSampleSize = heightRatio;
            } else {
                opts.inSampleSize = widthRatio;
            }
        }

        opts.inJustDecodeBounds = false;

        opts.inSampleSize = widthRatio;

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(imagePath, opts);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bmp;
    }
}
