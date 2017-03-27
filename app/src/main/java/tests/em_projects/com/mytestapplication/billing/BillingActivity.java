package tests.em_projects.com.mytestapplication.billing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal muchtar on 23/03/2017.
 */

public class BillingActivity extends Activity {
    private static final String TAG = "BillingActivity";

    private Button buyItemButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Log.d(TAG, "onCreate");

        buyItemButton = (Button) findViewById(R.id.buyItemButton);
        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
            }
        });
    }
}
