package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/27/16.
 */

public class ActivityFragmentColumnsGauge extends Activity {

    private static final String TAG = "ActivityFrgColumnsGauge";

    private ColumnsGaugeFragment columnsGaugeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_columns_gauge);

        // Init fragment
        columnsGaugeFragment = new ColumnsGaugeFragment();

        //set Data
        String[] titles = {"2015", "2016"};
        int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY};
        Float[] values = new Float[]{1000F, 1010F, 1020F, 1030F, 2000F, 2010F, 2020F, 2030F};
        try {
            columnsGaugeFragment.setData(new ArrayList<Float>(Arrays.asList(values)), new ArrayList<String>(Arrays.asList(titles)), 4, colors);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onResume", e);
        }

        // Add the fragment to the view
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activityFragmentColumnsGaugeLayout, columnsGaugeFragment);
        fragmentTransaction.commit();
    }
}
