package tests.em_projects.com.mytestapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by eyal muchtar on 16/04/16.
 */
public class ImageUtils {

    public static Bitmap loadBitmap(Context context, int resId) {
        if (context == null) {
            throw new NullPointerException("Context is null");
        }
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static Bitmap imageCropper(Bitmap orgBitmap, int xPos, int yPos, int width, int height) throws IllegalArgumentException {
        if (xPos < 0 || yPos < 0) {
            throw new IllegalArgumentException("position can not be < 0");
        }
        if (xPos + width > orgBitmap.getWidth()) {
            throw new IllegalArgumentException("Requiered image width is over the original image width");
        }
        if (yPos + height > orgBitmap.getHeight()) {
            throw new IllegalArgumentException("Requiered image height is over the original image height");
        }
        return Bitmap.createBitmap(orgBitmap, xPos, yPos, width, height);
    }
}
