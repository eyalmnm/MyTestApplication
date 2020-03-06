package tests.em_projects.com.mytestapplication.viewPage;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by eyal on 26/04/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("MyPagerAdapter", "getItem position: " + position);
        switch (position % 3) {
            case 0: // Fragment # 0 - This will show FirstFragment
                Bundle args = new Bundle();
                args.putString(TheFragment.TITLE, "Page # 1");
                args.putInt(TheFragment.NUMBER, position);
                TheFragment fr = new TheFragment();
                fr.setArguments(args);
                return fr;
            case 1: // Fragment # 0 - This will show FirstFragment different title
                args = new Bundle();
                args.putString(TheFragment.TITLE, "Page # 2");
                args.putInt(TheFragment.NUMBER, position);
                fr = new TheFragment();
                fr.setArguments(args);
                return fr;
            case 2: // Fragment # 1 - This will show SecondFragment
                args = new Bundle();
                args.putString(TheFragment.TITLE, "Page # 3");
                args.putInt(TheFragment.NUMBER, position);
                fr = new TheFragment();
                fr.setArguments(args);
                return fr;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return ViewPagerActivity.PAGES * ViewPagerActivity.LOOPS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
