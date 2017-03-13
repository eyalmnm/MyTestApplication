package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/9/16.
 */

public class ArcGaugeActivity extends Activity {

    public static final String TAG = "ArcGaugeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_arc_gauge);
    }
}
