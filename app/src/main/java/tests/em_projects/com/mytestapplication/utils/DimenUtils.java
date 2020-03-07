package tests.em_projects.com.mytestapplication.utils;

import android.content.res.Resources;

/**
 * Created by Eyal Muchtar on 18/05/2017.
 */

public class DimenUtils {

    public static float dpToPx(int dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(int px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
