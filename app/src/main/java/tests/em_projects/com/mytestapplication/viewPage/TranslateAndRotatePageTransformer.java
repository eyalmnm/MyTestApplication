package tests.em_projects.com.mytestapplication.viewPage;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by eyal on 26/04/16.
 */
public class TranslateAndRotatePageTransformer implements ViewPager.PageTransformer {

    private static final String TAG = "PageTransformer";
    private static final int PADDING_FACTOR = 50;

    @Override
    public void transformPage(View page, float position) {
        if (position >= 0)
            Log.d(TAG, "pos: (" + position + ") calc: " +  (1 - Math.abs(position)));
        page.setPadding(0, (int) (PADDING_FACTOR - Math.abs(position * PADDING_FACTOR)), 0, 0);
    }
}
