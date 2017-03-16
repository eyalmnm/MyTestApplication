package tests.em_projects.com.mytestapplication.activity_recognition;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 16/03/2017.
 * <p>
 * This Activity starts the Activity Recognition for detecting user's activity
 * this Activity uses the com.google.android.gms.permission.ACTIVITY_RECOGNITION permission
 */

public class ActivityRecognizedActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ActivityRecognizedActiv";

    private GoogleApiClient apiClient;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        // Layout is not necessary currently since we're using Log for display.
        setContentView(R.layout.activity_activity_recognition);

        // Init the API client
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Connect to Google Play Services
        apiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        // After connection with Google Play Services launch the Activity Recognition Service
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // The Activity will be recognized every 3 seconds and sent to Activity Recognition Service
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, 3000, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        // Stop updating activities after application termination
        if (null != pendingIntent && null != pendingIntent) {
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient, pendingIntent);
            apiClient.disconnect();
            pendingIntent = null;
            apiClient = null;
        }
    }
}
