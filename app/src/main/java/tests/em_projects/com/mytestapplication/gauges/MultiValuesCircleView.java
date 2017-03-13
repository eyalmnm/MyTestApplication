package tests.em_projects.com.mytestapplication.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by eyalmuchtar on 27/07/2016.
 */

// Ref: http://stackoverflow.com/questions/8489990/how-to-set-color-using-integer
// Ref: https://developer.android.com/reference/android/graphics/Color.html
// Ref: http://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android

public class MultiValuesCircleView extends View {

    private static final String TAG = "MultiValuesCircleView";

    private int width;
    private int height;
    private int radius;

    private int textSize;
    private int strokeWidth;

    private ArrayList<Paint> paints;
    private Paint textPaint;

    private ArrayList<Float> values;
    private float sumValue;
    private int[] colors;

    public MultiValuesCircleView(Context context) {
        super(context);

        init();
    }

    public MultiValuesCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MultiValuesCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        Log.d(TAG, "init");
        initTextPaint();
    }

    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width != height) throw new RuntimeException("WIDTH AND HEIGHT MUST BE EQUAL IN " + TAG + "!!!!");
        radius = (int) ((Math.min(height, width) /*/ 1.2F */) / 2F);
        strokeWidth = (int) (radius / 2F);
        textSize = (int) (strokeWidth / 5F);

        refreshTextPaint(textSize);
        refreshPaints(strokeWidth);
        invalidate();
    }

    private void refreshTextPaint(int textSize) {
        textPaint.setTextSize(textSize);
    }

    private void refreshPaints(int strokeWidth) {
        if (null != paints) {
            for (int i = 0; i < paints.size(); i ++) {
                paints.get(i).setStrokeWidth(strokeWidth / 2);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF circleBound = new RectF(strokeWidth / 4, strokeWidth / 4, width - (strokeWidth / 4), height - (strokeWidth / 4));

        int startDegree = 0;
        for (int i = 0; i < paints.size(); i++) {
            int degree = (int) valueToDegrees(values.get(i), sumValue);
            if (i == (paints.size() - 1)) {
                degree = 362 - startDegree;
            }
            drawArcAndValue(canvas, circleBound, radius, startDegree, degree, paints.get(i), values.get(i), colors[i], strokeWidth);
            startDegree += degree;
        }
    }

    private void drawArcAndValue(Canvas canvas, RectF circleBound, int radius, int startDegree, int degree, Paint paint, Float value, int color, int strokeWidth) {
        drawArc(canvas, circleBound, startDegree, degree, paint);
        drawValue(canvas, startDegree, degree, value, color, strokeWidth);
    }

    private void drawValue(Canvas canvas, int startDegree, int degree, Float value, int color, int strokeWidth) {
        Log.d(TAG, "drawValue");
        canvas.save();
        textPaint.setColor(revertColor(color));
        int angel = 90 + startDegree + (degree / 2);
        Log.d(TAG, "drawValue angel: " + angel);
        canvas.rotate(angel, radius, radius);
        canvas.drawText(String.valueOf(value), radius, (strokeWidth / 3), textPaint);
        canvas.restore();
    }

    private void drawArc(Canvas canvas, RectF circleBound, int startDegree, int degree, Paint paint) {
        canvas.drawArc(circleBound, startDegree, degree, false, paint);
    }

    public void setData(ArrayList<Float> values, int[] colors) {
        if (false == isValidData(values, colors)) {
            throw new RuntimeException("Invalid Data");
        }
        this.values = values;
        sumValue = getSumValue(values);
        this.colors = colors;

        reconstructPaints(colors);
        invalidate();
    }

    private void reconstructPaints(int[] colors) {
        paints = new ArrayList<>(colors.length);
        for (int i = 0; i < colors.length; i++) {
            paints.add(createPaint(colors[i]));
            paints.get(i).setStrokeWidth(strokeWidth / 2);
        }
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        return paint;
    }

    private float getSumValue(ArrayList<Float> values) {
        float sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i);
        }
        return sum;
    }

    private boolean isValidData(ArrayList<Float> values, int[] colors) {
        if (null != values && null != colors) {
            return values.size() == colors.length;
        }
        return false;
    }


    private float valueToDegrees(float value, float sumValue) {
        return (value * 360 / sumValue);
    }

    public static int revertColor(int color) {
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = Color.green(color);
        return Color.argb(255, red, green, blue);
    }
}
