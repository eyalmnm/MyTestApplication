package tests.em_projects.com.mytestapplication.viewPage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 26/04/16.
 */

// Ref: https://github.com/codepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
// Ref: http://developer.android.com/training/animation/screen-slide.html
// Ref: http://usefulcodeforandroid.blogspot.co.il/2012/08/creating-circular-pageviewer.html

public class ViewPagerActivity extends FragmentActivity {

    private static final String TAG = "ViewPagerActivity";

    public final static int PAGES = 3;
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;

    private ViewPager mPager;
    private MyPagerAdapter mMyPagerAdapter;

    private ArrayList<TheFragment> mTheFragments = new ArrayList<>();

    private Handler timeHandler;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new TranslateAndRotatePageTransformer());
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mMyPagerAdapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        mPager.setCurrentItem(FIRST_PAGE);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        mPager.setOffscreenPageLimit(3);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position: " + position + "  positionOffset: " + positionOffset + "  positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged state: " + state);
            }
        });

        timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "postDelayed");
                updateListeners(counter);
                counter++;
                timeHandler.postDelayed(this, 100);
            }
        }, 500);
    }

    private void updateListeners(int counter) {
        if (mTheFragments != null && !mTheFragments.isEmpty()) {
            for (TheFragment frg : mTheFragments) {
                frg.updateLabel(counter);
            }
        }
    }

    public int addListener(TheFragment theFragment) {
        if (!mTheFragments.contains(theFragment)) {
            mTheFragments.add(theFragment);
        }
        theFragment.updateLabel(counter);
        return counter;
    }

//    public void removeListener(TheFragment theFragment) {
//        if (theFragments.contains(theFragment)) {
//            theFragments.remove(theFragment);
//        }
//    }
}
