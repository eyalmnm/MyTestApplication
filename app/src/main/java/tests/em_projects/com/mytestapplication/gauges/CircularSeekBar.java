package tests.em_projects.com.mytestapplication.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eyal on 25/04/16.
 */

// @Ref http://stackoverflow.com/questions/4074937/android-how-to-get-a-custom-views-height-and-width
// @Ref http://stackoverflow.com/questions/2655402/android-canvas-drawtext
// @Ref http://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android

public class CircularSeekBar extends View {

    // Arrtibutes
    private int mBgColor;
    private int mMainCircleColor;
    private int mOuterCircleColor;
    private int mCircleWidth;
    private int mTextColor;
    private int mTextSize;

    // Paints
    private Paint mBgPaint;
    private Paint mMainCirclePaint;
    private Paint mOuterCirclePaint;
    private Paint mTextPaint;

    private int mWidth;
    private int mHeight;
    private int mAngel;
    private String mText = "0";

    public CircularSeekBar(Context context) {
        super(context);
    }

    public CircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Read properties from Attributes
        mBgColor = 0x00FFFFFF;
        mMainCircleColor = 0xFF006600;
        mOuterCircleColor = 0xFF666666;
        mCircleWidth = 5;
        mTextColor = 0xFFFFFFFF;
        mTextSize = 100;

        // Init the Paint
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);

        mMainCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMainCirclePaint.setColor(mMainCircleColor);
        mMainCirclePaint.setStyle(Paint.Style.STROKE);

        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setColor(mOuterCircleColor);
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    public void setTextPercents(int percents) {
        mAngel = (int) (percents * (360 / 100F));
        mText = String.valueOf(percents);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        ;
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        ;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the Bg Rect
        canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);

        // draw the Main circle
        int radius = Math.min(mWidth, mHeight) / 2;
        if (mCircleWidth < (radius / 10)) {
            mCircleWidth = (int) (radius / 10F);
        }
        RectF innerBound = new RectF(2 * mCircleWidth, 2 * mCircleWidth, 2 * (radius - mCircleWidth), 2 * (radius - mCircleWidth));
        mMainCirclePaint.setStrokeWidth(mCircleWidth);
        canvas.drawArc(innerBound, 0, 360, true, mMainCirclePaint);

        // Draw the outer circle
        mOuterCirclePaint.setStrokeWidth(mCircleWidth);
        canvas.drawArc(innerBound, -90, mAngel, false, mOuterCirclePaint); // TODO

        // Draw the text
        Rect bounds = new Rect();
        String str = mText + "%";
        if (mTextSize > (radius * 0.3F)) {
            mTextSize = (int) (radius * 0.3F);
        }
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, radius - (bounds.width() / 2F), radius + (bounds.height() / 2F), mTextPaint);
    }

}
