package tests.em_projects.com.mytestapplication.adjustResize;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ScrollView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by USER on 15/05/2017.
 */

public class AdjustResizeActivity extends Activity {
    private static final String TAG = "AdjustResizeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_resize);
        ScrollView sv = (ScrollView) findViewById(R.id.smsAuthScroll);
        sv.setEnabled(false);
    }
}
