package tests.em_projects.com.mytestapplication.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eyalmuchtar on 6/9/16.
 */

// Ref: http://stackoverflow.com/questions/4381033/multi-gradient-shapes

public class ArcGaugeView extends View {

    private static final String TAG = "ArcGaugeView";

    // Arc properties
    private Paint arcStartPaint;
    private Paint arcMiddlePaint;
    private Paint arcEndPaint;
    private int startColor;
    private int middleColor;
    private int endColor;

    // Hand properties
    private Paint handPaint;
    private int handColor;
    private int handWidth;

    // View properties
    private int width;
    private int height;
    private int radius;

    private float angle = 0;

    public ArcGaugeView(Context context) {
        super(context);

        init();
    }

    public ArcGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ArcGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        // Init Arc's Paint
        startColor = Color.GREEN;
        middleColor = Color.YELLOW;
        endColor = Color.RED;
        arcStartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcStartPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcMiddlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcMiddlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcEndPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcEndPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Init Hands Paint
        handColor = Color.BLACK;
        handWidth = 5;
        handPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handPaint.setColor(handColor);
        handPaint.setStyle(Paint.Style.STROKE);
        handPaint.setStrokeWidth(handWidth);
        handPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        radius = (int) ((Math.min(height * 2.5, width) / 1.2F) / 2F);

//        LinearGradient gradient = new LinearGradient(0, 0, width, height,
//                new int[] {
//                        Color.BLACK,
//                        Color.WHITE,
//                        Color.RED }, //substitute the correct colors for these
//                new float[] {0.5F, 0.8F, 1},
//                Shader.TileMode.CLAMP);
//
//        LinearGradient gradient = new LinearGradient(0, 0, width, height,
//                new int[] {
//                        0xFF1e5799,
//                        0xFF207cca,
//                        0xFF2989d8,
//                        0xFF207cca }, //substitute the correct colors for these
//                new float[] {0, 0.40f, 0.60f, 1 },
//                Shader.TileMode.CLAMP);
//
        RadialGradient gradient = new RadialGradient(
                width / 2,
                height / 2,
                (int) (1.5F * radius),
                new int[]{middleColor, endColor},
                new float[]{0.0f, 1f},
                Shader.TileMode.CLAMP);
//        arcPaint.setShader(gradient);
        arcStartPaint.setStrokeWidth(radius / 2);
        arcStartPaint.setColor(startColor);
        arcMiddlePaint.setStrokeWidth(radius / 2);
        arcMiddlePaint.setColor(middleColor);
        arcEndPaint.setStrokeWidth(radius / 2);
        arcEndPaint.setColor(endColor);
//        arcEndPaint.setShader(gradient);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        RectF arcBoundRec = new RectF(radius / 2, radius / 2, 2 * radius, 2 * radius);
        RectF arcBoundRec = new RectF(radius / 4, height - radius, width - (radius / 4), height + (radius));
//        canvas.drawRect(arcBoundRec, handPaint);
        canvas.drawArc(arcBoundRec, -122, 64, false, arcMiddlePaint);
        canvas.drawArc(arcBoundRec, -180, 60, false, arcStartPaint);
        canvas.drawArc(arcBoundRec, -60, 60, false, arcEndPaint);
        float currAngle = 270 - angle;
//        canvas.rotate(- angle, 1.2F * radius, 1.2F * radius)
        float handXpos = calculateXpos((float) (radius), currAngle);
        float handYPos = calculateYpos(radius, currAngle);
        canvas.drawLine((float) (1.2 * radius) + handXpos, height + handYPos - 2, (float) (1.2 * radius), height - 2, handPaint);
//        canvas.drawRect(arcBoundRec, handPaint);
//        canvas.restore();
        canvas.drawCircle((float) (1.2 * radius), (float) (height - 2), 5, handPaint);
    }

    private float calculateYpos(float length, float angle) {
        return (float) (Math.cos(Math.toRadians(angle)) * length);
    }

    private float calculateXpos(float length, float angle) {
        return (float) (Math.sin(Math.toRadians(angle)) * length);
    }

    public void setAngle(float anAngle) {
        angle = anAngle;
        invalidate();
    }
}
