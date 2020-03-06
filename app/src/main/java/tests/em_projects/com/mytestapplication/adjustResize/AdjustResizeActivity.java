package tests.em_projects.com.mytestapplication.adjustResize;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

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

        // Make sure the view adjust while showing keyboard
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ScrollView sv = findViewById(R.id.smsAuthScroll);
        sv.setEnabled(false);
    }
}
