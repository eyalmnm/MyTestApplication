package tests.em_projects.com.mytestapplication.battery_save_mode_detect;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;

import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED;
import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED;
import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED;

/**
 * Created by eyal muchtar on 18/07/2017.
 */

public class BatterySafeMode_Activity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_safe_mode);

        TextView textView = (TextView) findViewById(R.id.textView);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager.isActiveNetworkMetered() && network.getType() != ConnectivityManager.TYPE_WIFI) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                switch (connectivityManager.getRestrictBackgroundStatus()) {
                    case RESTRICT_BACKGROUND_STATUS_ENABLED:
                        // Background data usage is blocked for this app. Wherever possible,
                        // the app should also use less data in the foreground.
                        Intent intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                        textView.setText("RESTRICT_BACKGROUND_STATUS_ENABLED");
                        break;
                    case RESTRICT_BACKGROUND_STATUS_WHITELISTED:
                        // The app is whitelisted. Wherever possible,
                        // the app should use less data in the foreground and background.
                        textView.setText("RESTRICT_BACKGROUND_STATUS_WHITELISTED");
                        break;
                    case RESTRICT_BACKGROUND_STATUS_DISABLED:
                        // Data Saver is disabled. Since the device is connected to a
                        // metered network, the app should use less data wherever possible.
//                        intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
//                        intent.setData(Uri.parse("package:" + getPackageName()));
//                        startActivity(intent);
                        textView.setText("RESTRICT_BACKGROUND_STATUS_DISABLED");
                        break;
                }
            }
        }
    }
}
