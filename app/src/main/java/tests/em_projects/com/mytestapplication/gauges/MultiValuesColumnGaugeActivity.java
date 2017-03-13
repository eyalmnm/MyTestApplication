package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 17/07/2016.
 */
public class MultiValuesColumnGaugeActivity extends Activity {

    private MultiValuesColumnGaugeView theGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_columns_gauge);

        theGauge = (MultiValuesColumnGaugeView) findViewById(R.id.theGauge);

        String[] titlesArr = {"2015", "2016"};
        int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY};
        Float[] values = new Float[]{1000F, 1010F, 1020F, 1030F, 2000F, 2010F, 2020F, 2030F};
        ArrayList<String> titles = new ArrayList<>(Arrays.asList(titlesArr));
        ArrayList<Float> data = new ArrayList<Float>(Arrays.asList(values));
        try {
            theGauge.setData(data, titles, 4, colors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
