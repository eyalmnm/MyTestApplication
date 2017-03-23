package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 25/04/16.
 */
public class CircularSeekBarActivity extends Activity {

    private CircularSeekBar circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_seek_bar);
        circular = (CircularSeekBar) findViewById(R.id.circular);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                circular.setTextPercents(35);
            }
        }, 5000);
    }
}
