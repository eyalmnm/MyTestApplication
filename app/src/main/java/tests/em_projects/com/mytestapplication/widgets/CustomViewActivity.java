package tests.em_projects.com.mytestapplication.widgets;

import android.app.Activity;
import android.os.Bundle;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 04/05/16.
 */
public class CustomViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_text);
    }
}
