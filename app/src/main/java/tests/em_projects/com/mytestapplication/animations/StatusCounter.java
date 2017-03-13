package tests.em_projects.com.mytestapplication.animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;


public class StatusCounter extends FrameLayout {

    private static final String TAG = "StatusCounter";

    private static final int STEP_TIME = 100;

    private final int percentsColor = getResources().getColor(R.color.grey_listenapp);
    private final int circleColor = getResources().getColor(R.color.orange_listenapp);

    private TextView titleText;
    private TextView counterText;

    private Paint circlePaint;
    private Paint basePaint;

    private int currentPercents = 0;
    private int requiredPercents = 0;

    public StatusCounter(Context context) {
        this(context, null, 0, 0);
    }

    public StatusCounter(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public StatusCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StatusCounter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);

        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColor(Color.BLACK);
        basePaint.setStyle(Paint.Style.FILL);

        ViewGroup content = (ViewGroup) View.inflate(context, R.layout.view_counter_content, null);
        titleText = (TextView) content.findViewById(R.id.title);
        counterText = (TextView) content.findViewById(R.id.counter);
        addView(content);
    }

    public String getTitle() {
        return titleText.getText().toString();
    }

    public void setTitle(String title) {
        titleText.setText(title);
        invalidate();
    }

    public int getCurrentPercents() {
        return currentPercents;
    }

    public void setCurrentPercents(int currentPercents) {
        this.currentPercents = currentPercents;
        counterText.setText(String.valueOf(currentPercents) + "%");
        invalidate();
    }

    public int getRequiredPercents() {
        return requiredPercents;
    }

    public void setRequiredPercents(int requiredPercents) {
        this.requiredPercents = requiredPercents;
        invalidate();
    }

    public void start() {
        postDelayed(DecrementTask, STEP_TIME);
    }

    public void stop() {
        removeCallbacks(DecrementTask);
    }

    public void reset() {
        setRequiredPercents(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        float outerCircleWidth = radius / 15f;

        // Ref: http://stackoverflow.com/questions/17954596/how-to-draw-circle-by-canvas-in-android
        canvas.drawCircle(radius, radius, radius, basePaint);

        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(outerCircleWidth);
        canvas.drawCircle(radius, radius, radius - outerCircleWidth, circlePaint);

        RectF innerBound = new RectF(outerCircleWidth, outerCircleWidth, 2 * (radius) - outerCircleWidth, 2 * (radius) - outerCircleWidth);

        // TODO Temove this block
        Paint testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setColor(Color.YELLOW);
//        canvas.drawRect(innerBound, testPaint);

        int angle = Math.round((currentPercents / (float) 100) * 360);
        circlePaint.setColor(percentsColor);
        canvas.drawArc(innerBound, -90, 360 - angle, false, circlePaint);
    }

    private Runnable DecrementTask = new Runnable() {
        @Override
        public void run() {
            if (currentPercents !=  requiredPercents) {
                int step = currentPercents < requiredPercents ? 1 : -1;
                setCurrentPercents(getCurrentPercents() + step);
                invalidate();
            } else {
                stop();
            }
            postDelayed(this, STEP_TIME);
        }
    };
}
