package tests.em_projects.com.mytestapplication.plot;

import android.app.Activity;
import android.os.Bundle;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by Eyal.Muchtar on 10/04/2016.
 */
public class PlotActivity extends Activity {

    private DynamicPlot mDynamicPlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        mDynamicPlot = (DynamicPlot) findViewById(R.id.plot);
        mDynamicPlot.start();
    }
}
