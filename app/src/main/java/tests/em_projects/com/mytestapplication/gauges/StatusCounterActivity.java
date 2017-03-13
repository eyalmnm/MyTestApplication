package tests.em_projects.com.mytestapplication.gauges;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 04/05/16.
 */

// Ref: http://stackoverflow.com/questions/13056331/android-custom-layout-ondraw-never-gets-called
public class StatusCounterActivity extends Activity {

    private static final String TAG = "StatusCounterActivity";

    private final static int DELAY_INTERVAL = 1000 * 60; //1 minute
    public static Handler handler = new Handler();
    private StatusCounter statusCounter;
    private int currentTime = 1;
    //get information about counters and update them
    Runnable updateCounter = new Runnable() {
        @Override
        public void run() {
            setCounter();
            handler.postDelayed(updateCounter, DELAY_INTERVAL);
        }
    };

    private float minFactor = 1F;
    private float maxFactor = 1.1F;
    private final ScaleAnimation growAnim = new ScaleAnimation(minFactor, maxFactor, minFactor, maxFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
    private final ScaleAnimation shrinkAnim = new ScaleAnimation(maxFactor, minFactor, maxFactor, minFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);

    private ImageView circleShapeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_counter);

        circleShapeImage = (ImageView) findViewById(R.id.circleShapeImage);

        statusCounter = (StatusCounter) findViewById(R.id.statusCounter);
        statusCounter.setTitle("Test");
        statusCounter.setMaxTime(10);  // max time Minutes
        statusCounter.setCurrentTime(currentTime); // start time Minutes
        statusCounter.setChangeValue(75); //
        statusCounter.setChangeType(StatusCounter.ChangeType.VALUE);
        statusCounter.setWillNotDraw(false);
        statusCounter.start();
        handler.postDelayed(updateCounter, DELAY_INTERVAL);

        initBitRateAnimation();
    }

    private void initBitRateAnimation() {

        growAnim.setDuration(2000);
        shrinkAnim.setDuration(2000);

        circleShapeImage.setAnimation(growAnim);
        growAnim.start();

        growAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circleShapeImage.setAnimation(shrinkAnim);
                shrinkAnim.start();
            }
        });
        shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circleShapeImage.setAnimation(growAnim);
                growAnim.start();
            }
        });
    }

    private void setCounter() {
        statusCounter.setCurrentTime(++currentTime);
        if (currentTime == 3) {
            shrinkAnim.setDuration(500);
            growAnim.setDuration(500);
        }
        if (currentTime == 5) {
            shrinkAnim.setDuration(50);
            growAnim.setDuration(50);
        }
        Log.d(TAG, "setCounter() currentTime: " + currentTime);
    }
}
