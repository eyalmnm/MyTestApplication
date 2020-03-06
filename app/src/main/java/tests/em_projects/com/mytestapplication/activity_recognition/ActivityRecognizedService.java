package tests.em_projects.com.mytestapplication.activity_recognition;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal muchtar on 16/03/2017.
 * <p>
 * This service used for handle the Activity Recognition
 */

public class ActivityRecognizedService extends IntentService {

    private static final String TAG = "ActivityRecognizedServ";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ActivityRecognizedService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        Log.d(TAG, "handleDetectedActivities");
        for (DetectedActivity activity : probableActivities) {
            switch (activity.getType()) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.d(TAG, "In Vehicle: " + activity.getConfidence());
                    if (activity.getConfidence() >= 75) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you driving?");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    Log.d(TAG, "On Bicycle: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    Log.d(TAG, "On Foot: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.d(TAG, "Running: " + activity.getConfidence());
                    if (activity.getConfidence() >= 75) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you running?");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.d(TAG, "Still: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.TILTING: {
                    Log.d(TAG, "Tilting: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.d(TAG, "Walking: " + activity.getConfidence());
                    if (activity.getConfidence() >= 75) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                        builder.setContentText("Are you walking?");
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentTitle(getString(R.string.app_name));
                        NotificationManagerCompat.from(this).notify(0, builder.build());
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.d(TAG, "Unknown: " + activity.getConfidence());
                    break;
                }
            }
        }
    }
}
