package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/13/16.
 */

public class ColumnsGaugeActivity extends Activity {

    private static final String TAG = "ColumnsGaugeActivity";

    private ColumnsGaugeView theGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_columns_gauge);
        Log.d(TAG, "onCreate");

        theGauge = (ColumnsGaugeView) findViewById(R.id.theGauge);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] titles = {"2015", "2016"};
        int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY};
        Float[] values = new Float[]{1000F, 1010F, 1020F, 1030F, 2000F, 2010F, 2020F, 2030F};
        try {
            theGauge.setData(new ArrayList<Float>(Arrays.asList(values)), new ArrayList<String>(Arrays.asList(titles)), 4, colors);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onResume", e);
        }
    }
}
