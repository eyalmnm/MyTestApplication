package tests.em_projects.com.mytestapplication.viewPage;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by eyal on 26/04/16.
 */
public class TranslatAndRotatePageTransformer implements ViewPager.PageTransformer {

    private static final String TAG = "PageTransformer";

    @Override
    public void transformPage(View page, float position) {
        if (position >= 0)
            Log.d(TAG, "pos: (" + position + ") calc: " +  (1 - Math.abs(position)));
    }
}
