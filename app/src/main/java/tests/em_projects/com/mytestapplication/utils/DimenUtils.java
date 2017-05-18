package tests.em_projects.com.mytestapplication.utils;

import android.content.res.Resources;

/**
 * Created by Eyal Muchtar on 18/05/2017.
 */

public class DimenUtils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
