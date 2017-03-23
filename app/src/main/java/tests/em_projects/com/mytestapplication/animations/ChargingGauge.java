package tests.em_projects.com.mytestapplication.animations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eyalmuchtar on 6/7/16.
 */

public class ChargingGauge extends FrameLayout {

    private static final String TAG = "ChargingGauge";

    private static final int timeInterval = 1000; // 1 Sec

    // drawing elements
    private int width;
    private int height;

    private int level = 0;

    // Animations
    private ScaleAnimation firstGrawAnimation;
    private ScaleAnimation shrinkAnimation;
    private ScaleAnimation growAnimation;
    private AlphaAnimation fadeInAnimation;
    private ScaleAnimation chargeLevelAnimation;

    // Animation properties
    private float maxFactor = 1.2F;

    private boolean animIsRunning = false;

    // UI Elements
    private View chargingLevelView;
    private ImageView fullCircleImageView;
    private ImageView beatingRingImageView;
    private TextView batteryLevelTextView;

    // Animated Drawables
    private GradientDrawable fullCircleDrawable;
    private GradientDrawable beatingRingDrawable;
    private Animation.AnimationListener firstGrowAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "firstGrowAnimationListener -> onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.d(TAG, "firstGrowAnimationListener -> onAnimationEnd");
            animIsRunning = false;
            if (View.VISIBLE == getVisibility()) {
                animIsRunning = true;
                fullCircleImageView.setVisibility(INVISIBLE);
                beatingRingImageView.setVisibility(VISIBLE);
                batteryLevelTextView.setVisibility(VISIBLE);
                batteryLevelTextView.setAnimation(fadeInAnimation);
                fadeInAnimation.start();
                beatingRingImageView.setAnimation(shrinkAnimation);
                shrinkAnimation.start();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };
    private Animation.AnimationListener shrinkAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // Log.d(TAG, "shrinkAnimationListener");
            animIsRunning = false;
            if (View.VISIBLE == getVisibility()) {
                animIsRunning = true;
                beatingRingImageView.setAnimation(growAnimation);
                growAnimation.start();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };
    private Animation.AnimationListener growAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // Log.d(TAG, "growAnimationListener");
            animIsRunning = false;
            if (View.VISIBLE == getVisibility()) {
                animIsRunning = true;
                beatingRingImageView.setAnimation(shrinkAnimation);
                shrinkAnimation.start();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    public ChargingGauge(Context context) {
        super(context);

        initAll();
    }

    public ChargingGauge(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAll();
    }

    public ChargingGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAll();
    }

    private void initAll() {
        Log.d(TAG, "initAll");
        setMeasureAllChildren(true);

        setBackgroundColor(Color.argb(255, 221, 221, 221));

        // Init Elements
        initElements();

        // Init Drawables
        initDrawables();

        // Shrink All
        shrinkAll();

        // Init Animation
        initAnimations();
    }

    private void initElements() {
        Log.d(TAG, "initElements");

        chargingLevelView = new View(getContext());
        chargingLevelView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, Gravity.BOTTOM));
        chargingLevelView.setBackgroundColor(Color.TRANSPARENT);
        addView(chargingLevelView);

        fullCircleImageView = new ImageView(getContext());
        fullCircleImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        fullCircleImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        addView(fullCircleImageView);

        beatingRingImageView = new ImageView(getContext());
        beatingRingImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        beatingRingImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        addView(beatingRingImageView);

        batteryLevelTextView = new TextView(getContext());
        batteryLevelTextView.setMinEms(4);
        batteryLevelTextView.setText("0%");
        batteryLevelTextView.setGravity(Gravity.CENTER);
        addView(batteryLevelTextView);
    }

    private void initDrawables() {
        Log.d(TAG, "initDrawables");
        fullCircleDrawable = new GradientDrawable();
        fullCircleDrawable.setShape(GradientDrawable.OVAL);
        fullCircleDrawable.setColor(Color.WHITE);

        beatingRingDrawable = new GradientDrawable();
        beatingRingDrawable.setShape(GradientDrawable.OVAL);
        beatingRingDrawable.setColor(Color.TRANSPARENT);
    }

    private void shrinkAll() {
        Log.d(TAG, "shrinkAll");
        fullCircleImageView.setVisibility(VISIBLE);
        beatingRingImageView.setVisibility(INVISIBLE);
        batteryLevelTextView.setVisibility(INVISIBLE);
    }

    private void initAnimations() {
        Log.d(TAG, "initAnimations");
        firstGrawAnimation = new ScaleAnimation(0.0f, maxFactor, 0.0f, maxFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        shrinkAnimation = new ScaleAnimation(maxFactor, 1.0f, maxFactor, 1.0f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        growAnimation = new ScaleAnimation(1.0f, maxFactor, 1.0f, maxFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        fadeInAnimation = new AlphaAnimation(0.0F, 1.0F);
        chargeLevelAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0F, Animation.RELATIVE_TO_SELF, 1.0F);

        firstGrawAnimation.setDuration(timeInterval * 3);
        shrinkAnimation.setDuration(timeInterval);
        growAnimation.setDuration(timeInterval);
        fadeInAnimation.setDuration(timeInterval);
        chargeLevelAnimation.setDuration(timeInterval * 3);

        firstGrawAnimation.setAnimationListener(firstGrowAnimationListener);
        shrinkAnimation.setAnimationListener(shrinkAnimationListener);
        growAnimation.setAnimationListener(growAnimationListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
        width = MeasureSpec.getSize(widthMeasureSpec); // Return pixels
        height = MeasureSpec.getSize(heightMeasureSpec); // Returs pixels
        fullCircleImageView.setLayoutParams(new LayoutParams(width, height, Gravity.CENTER));
        beatingRingImageView.setLayoutParams(new LayoutParams(width, height, Gravity.CENTER));
        batteryLevelTextView.setLayoutParams(new LayoutParams(width, height, Gravity.CENTER));
        int drawableShapsSize = (width < height ? width : height);
        updateGradientDrawable(drawableShapsSize);

        if (false == animIsRunning) {
            animIsRunning = true;
            shrinkAll();
            fullCircleImageView.setAnimation(firstGrawAnimation);
            firstGrawAnimation.start();
        }
    }

    private void updateGradientDrawable(int drawableShapsSize) {
        Log.d(TAG, "updateGradientDrawable");
        int radius = (int) (drawableShapsSize / (maxFactor + 0.2));
        fullCircleDrawable.setSize(radius, radius);
        fullCircleImageView.setImageDrawable(fullCircleDrawable);

        beatingRingDrawable.setSize(radius, radius);
        beatingRingDrawable.setStroke(radius / 4 , Color.WHITE);
        beatingRingImageView.setImageDrawable(beatingRingDrawable);

        batteryLevelTextView.setTextSize(radius / 25);
    }

    public void setBatteryLevel(int level, int color) {
        Log.d(TAG, "setBatteryLevel");
        batteryLevelTextView.setTextColor(Color.WHITE);
        batteryLevelTextView.setText(String.valueOf(level) + "%");
        chargingLevelView.setBackgroundColor(color);
        chargingLevelView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height * level / 100, Gravity.BOTTOM));
        if (0 == this.level) {
            chargingLevelView.setAnimation(chargeLevelAnimation);
            chargeLevelAnimation.start();
        }
        this.level = level;
    }
}
