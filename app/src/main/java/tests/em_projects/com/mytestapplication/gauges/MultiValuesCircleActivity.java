package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 27/07/2016.
 */
public class MultiValuesCircleActivity extends Activity {

    private MultiValuesCircleView theGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_values_circle);

        theGauge = (MultiValuesCircleView) findViewById(R.id.theGauge);

        final int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLACK};
        Float[] values = new Float[]{100F, 150F, 200F, 250F};
        theGauge.setData(new ArrayList<>(Arrays.asList(values)), colors);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Float[] values = new Float[]{150F, 100F, 250F, 200F};
                theGauge.setData(new ArrayList<>(Arrays.asList(values)), colors);
            }
        }, 5000);

    }
}
