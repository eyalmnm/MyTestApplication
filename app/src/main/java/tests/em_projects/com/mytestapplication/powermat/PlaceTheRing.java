package tests.em_projects.com.mytestapplication.powermat;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/13/16.
 */

// ref: http://stackoverflow.com/questions/9387711/android-animation-flicker

public class PlaceTheRing extends Activity {

    private static final String TAG = "PlaceTheRing";

    private TranslateAnimation translateAnimation;

    private View activityPlaceTheRingLayout;
    private ImageView activityPlaceTheRingPhoneImageView;
    private ImageView activityPlaceTheRingMatImageView;

    private int finalPhonePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_the_ring);

        activityPlaceTheRingLayout = findViewById(R.id.activityPlaceTheRingLayout);
        activityPlaceTheRingPhoneImageView = (ImageView) findViewById(R.id.activityPlaceTheRingPhoneImageView);
        activityPlaceTheRingMatImageView = (ImageView) findViewById(R.id.activityPlaceTheRingMatImageView);

        activityPlaceTheRingPhoneImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim();
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim();
            }
        }, 10000);
    }

    private void anim() {
        int phoneImageHeight = activityPlaceTheRingPhoneImageView.getHeight();
        if (0 != activityPlaceTheRingPhoneImageView.getTranslationY()) {
            activityPlaceTheRingPhoneImageView.setTranslationY(0);
        }
        finalPhonePosition = activityPlaceTheRingLayout.getHeight() + dpToPx(30) - phoneImageHeight - (activityPlaceTheRingMatImageView.getHeight() / 2);

        activityPlaceTheRingPhoneImageView.setVisibility(View.VISIBLE);
        translateAnimation = new TranslateAnimation(0F, 0F, -phoneImageHeight, finalPhonePosition);
        translateAnimation.setDuration(1000);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activityPlaceTheRingPhoneImageView.setTranslationY(finalPhonePosition);
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                activityPlaceTheRingPhoneImageView.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        activityPlaceTheRingPhoneImageView.startAnimation(translateAnimation);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
