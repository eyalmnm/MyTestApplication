package tests.em_projects.com.mytestapplication.animations;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/9/16.
 */

public class ChargingGaugeActivity extends Activity {

    private static final String TAG = "ChargingGaugeActivity";

    private ChargingGauge sampleChargingGauge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_charging_gauge);

        sampleChargingGauge = (ChargingGauge) findViewById(R.id.sampleChargingGauge);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sampleChargingGauge.setBatteryLevel(50, Color.RED);
            }
        }, 5000);
    }
}
