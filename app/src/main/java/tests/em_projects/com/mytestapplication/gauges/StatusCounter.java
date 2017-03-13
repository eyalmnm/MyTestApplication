package tests.em_projects.com.mytestapplication.gauges;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;

public class StatusCounter extends FrameLayout {

    private static final int STEP_TIME = 60 * 1000;
    //    private final int okColor = getResources().getColor(R.color.dashboard_ok);
//    private final int warningColor = getResources().getColor(R.color.dashboard_warning);
//    private final int emptyColor = getResources().getColor(R.color.dashboard_empty);
    private final int circleColor = 0xffe5c9df; //getResources().getColor(R.color.dashboard_circle);
    private final int circleColorFill = 0xffebd6e7; //getResources().getColor(R.color.dashboard_circle);
    //    private final int circleColor = getResources().getColor(R.color.dashboard_circle);
    private final int innerCircleColor = getResources().getColor(R.color.dashboard_inner_circle);
    private int maxTime;
    private int currentTime;
    private ChangeType changeType = ChangeType.VALUE;
    private int changeValue;
    private long timeInterval = STEP_TIME;
    private TextView titleText;
    private TextView counterText;
    private Paint circlePaint;
    private Paint circlePaintFill;
    private Runnable DecrementTask = new Runnable() {
        @Override
        public void run() {
            if (currentTime > 0) {
                invalidate();
            }
            postDelayed(this, timeInterval);
        }
    };

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
        setMeasureAllChildren(true);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaintFill.setStyle(Paint.Style.FILL);


        ViewGroup content = (ViewGroup) View.inflate(context, R.layout.ui_counter_content, null);
        titleText = (TextView) content.findViewById(R.id.title);
        counterText = (TextView) content.findViewById(R.id.counter);
        addView(content);
    }

    public void setTimeIntervalMilis(long timeInterval) {
        if (timeInterval > 50) {
            this.timeInterval = timeInterval;
        }
    }

    public String getTitle() {
        return titleText.getText().toString();
    }

    public void setTitle(String title) {
        titleText.setText(title);
        invalidate();
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
        invalidate();
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int time) {
        currentTime = time;
        counterText.setText("14" + time); // (StringUtils.formatTimeToString(time));
        invalidate();
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public int getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(int changeValue) {
        this.changeValue = changeValue;
    }

    public void start() {
        postDelayed(DecrementTask, timeInterval);
    }

    public void stop() {
        removeCallbacks(DecrementTask);
    }

    public void reset() {
        setCurrentTime(maxTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        float outerCircleWidth = radius / 10f;
        circlePaintFill.setColor(circleColorFill);
        circlePaintFill.setStrokeWidth(outerCircleWidth);
        canvas.drawCircle(radius, radius, radius - outerCircleWidth, circlePaintFill);
        circlePaint.setColor(circleColor);
        circlePaint.setStrokeWidth(outerCircleWidth);
        canvas.drawCircle(radius, radius, radius - outerCircleWidth, circlePaint);

        RectF innerBound = new RectF(2 * outerCircleWidth, 2 * outerCircleWidth, 2 * (radius - outerCircleWidth), 2 * (radius - outerCircleWidth));
        int angle = Math.round((currentTime / (float) maxTime) * 360);
        if (currentTime == 0) {
            circlePaint.setColor(innerCircleColor); // (emptyColor);
            canvas.drawArc(innerBound, 0, 360, false, circlePaint);
        } else {
            circlePaint.setColor(innerCircleColor);
//            switch (changeType) {
//                case PERCENTAGE:
//                    circlePaint.setColor(innerCircleColor); // (angle <= changeValue ? warningColor : okColor);
//                    break;
//                case VALUE:
//                    circlePaint.setColor(innerCircleColor); // (currentTime <= changeValue ? warningColor : okColor);
//                    break;
//            }
            canvas.drawArc(innerBound, -90, 360 - angle, false, circlePaint);
        }

        circlePaint.setColor(circleColor); // (innerCircleColor);
        circlePaint.setStrokeWidth(3);
        canvas.drawCircle(radius, radius, radius - 2.5f * outerCircleWidth, circlePaintFill);
        canvas.save();
        canvas.rotate(-angle, radius, radius);
//        canvas.drawLine(radius, .5f * outerCircleWidth, radius, 2.5f * outerCircleWidth, circlePaint);
        canvas.restore();
    }

    public enum ChangeType {PERCENTAGE, VALUE}
}
