package tests.em_projects.com.mytestapplication.gauges.with_timer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 12/03/2017.
 */

public class AutoStatusCounterActivity extends Activity {

    private static final String TAG = "AutoStatusCounterActivity";

    // UI Components
    private StatusCounter statusCounter;
//    private int currentTime = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_status_counter);

        statusCounter = (StatusCounter) findViewById(R.id.statusCounter);

        statusCounter.setTitle("Test");
        statusCounter.setRequiredPercents(80);
//        statusCounter.setMaxTime(10);  // max time Minutes
//        statusCounter.setCurrentTime(currentTime); // start time Minutes
//        statusCounter.setChangeValue(3); //
//        statusCounter.setChangeType(StatusCounter.ChangeType.VALUE);
        statusCounter.setWillNotDraw(false);
        statusCounter.start();

    }
}
