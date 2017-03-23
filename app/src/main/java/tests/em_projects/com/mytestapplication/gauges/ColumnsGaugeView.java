package tests.em_projects.com.mytestapplication.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by eyalmuchtar on 6/20/16.
 */

public class ColumnsGaugeView extends View {

    private static final String TAG = "ColumnsGuageView";

    private static final int MAX_COLUMN_WIDTH = 20;

    // All columns properties
    private Paint[] columnsPaints;
    private Paint titleTextPaint;
    private Paint valuesTextPaint;
    private int maxColumnWidth = 20;
    private int numberOfColumnsInGroup = 3;
    private int[] columnsColors = new int[]{Color.RED, Color.YELLOW, Color.GREEN};

    // Graph's layout properties
    private Paint layoutLinesPaint;
    private int layoutLinesColor;
    private int layoutLinesWidth;
    private int valuesTextColor;
    private int titleTextColor;

    // View properties
    private int width;
    private int height;
    private float graphHeight;
    private float graphStartX;
    private int titleHeight;

    // Data holders
    private ArrayList data;
    private ArrayList<String> titles;

    // View properties - not attributes
    private float maxValue;
    private float minValue;
    private int columnWidth;
    private int[] titlesWidth;
    private int[] valuesWidth;
    private float minValueY;
    private float maxValueY;

    public ColumnsGaugeView(Context context) {
        super(context);

        init();
    }

    public ColumnsGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ColumnsGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public static float getStringWidth(Paint textPaint, String text) {
        if ((null == text) || text.isEmpty()) return 0;
        return textPaint.measureText(text, 0, text.length());
    }

    private void init() {
        initLayoutComponents();
    }

    private void initLayoutComponents() {

        layoutLinesColor = Color.BLACK;
        layoutLinesWidth = 3;
        layoutLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layoutLinesPaint.setColor(layoutLinesColor);
        layoutLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        layoutLinesPaint.setStrokeWidth(layoutLinesWidth);

        valuesTextColor = Color.BLACK;
        valuesTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valuesTextPaint.setColor(valuesTextColor);

        titleTextColor = Color.BLACK;
        titleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valuesTextPaint.setColor(titleTextColor);

        columnsPaints = new Paint[numberOfColumnsInGroup];
        for (int i = 0; i < numberOfColumnsInGroup; i++) {
            columnsPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            columnsPaints[i].setColor(columnsColors[i]);
            columnsPaints[i].setStyle(Paint.Style.FILL_AND_STROKE);
            columnsPaints[i].setStrokeWidth(columnWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        titlesWidth = calculateTitlesWidth(titles);
        valuesWidth = calculateValuesWidth(maxValue, minValue);
        graphStartX = 1; // (float) (1.2 * Math.max(valuesWidth[0], valuesWidth[1])) + 1;  // TODO

        columnWidth = calculateColumnWidth(data);

        graphHeight = calculateGraphHeight(height, titles) - 1;
        maxValueY = calculateMaxValueY(graphHeight);
        minValueY = calculateMinValueY(graphHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null) return;

        drawColumns(canvas);
        drawGraphLayout(canvas);
        drawValues(canvas);
    }

    private void drawTitle(Canvas canvas, int x, int y, String title, Paint paint) {
        int calcX = x + (((3 * columnWidth) - titlesWidth[0]) / 2);
        canvas.drawText(title, calcX, y + titleHeight, paint);
    }

    private void drawValues(Canvas canvas) {
        // TODO
    }

    private void drawGraphLayout(Canvas canvas) {
        canvas.drawLine(graphStartX, graphHeight, width, graphHeight, layoutLinesPaint);    // Horizontal
        canvas.drawLine(graphStartX, 0, graphStartX, graphHeight, layoutLinesPaint);        // Vertical
    }

    private void drawColumns(Canvas canvas) {
        int groupCounter = 0;
        int x = (int) graphStartX + 2;
        for (int i = 0; i < data.size(); i++) {
            int modulo = i % numberOfColumnsInGroup;
            if ((0 < i) && (modulo == 0)) {
                x += columnWidth / 2;
                drawTitle(canvas, x + 2, (int) (graphHeight + 2), titles.get(groupCounter++), valuesTextPaint);
            } else if (0 == i) {
                drawTitle(canvas, x + 2, (int) (graphHeight + 2), titles.get(groupCounter++), valuesTextPaint);
            }
            drawGraphColumn(canvas, (Float) data.get(i), x, columnsPaints[modulo]);
            x += columnWidth;
        }
    }

    private void drawGraphColumn(Canvas canvas, float value, int x, Paint currentColumnPaint) {
        int y = calculateYValue(value);
        Rect rect = new Rect(x, (int) (((int) graphHeight) - y - minValueY), x + columnWidth, ((int) graphHeight));
        canvas.drawRect(rect, currentColumnPaint);
        canvas.save();
        int xPivot = x + 5;
        int yPivot = (int) (graphHeight * 3 / 4);
        canvas.rotate(90, xPivot, yPivot);
        canvas.drawText(String.valueOf(value), xPivot, yPivot, valuesTextPaint);
        canvas.restore();
    }

    private int calculateYValue(float value) {
        float yDiff = maxValueY - minValueY;
        float valDiff = maxValue - minValue;
        return (int) (((value - minValue) / valDiff) * yDiff);
    }

    public void setData(ArrayList<Float> data, ArrayList<String> titles, int numberOfColumnsInGroup, int[] groupColumnsColors) throws Exception {
        if (true == checkDataValidity(data, (ArrayList) titles, numberOfColumnsInGroup, groupColumnsColors)) {
            this.data = data;
            this.titles = titles;
            this.numberOfColumnsInGroup = numberOfColumnsInGroup;
            this.columnsColors = groupColumnsColors;
            initColumnsPaints();
            invalidate();
        }
    }

    private boolean checkDataValidity(ArrayList<Float> data, ArrayList titles, int numberOfColumnsInGroup, int[] groupColumnsColors) throws Exception {
        int groups = data.size() / numberOfColumnsInGroup;
        if (groups != titles.size()) {
            throw new Exception("Invalid number of titles or number of groups");
        }
        if (0 != (data.size() % numberOfColumnsInGroup)) {
            throw new Exception("Invalid number of number of groups");
        }
        if (groupColumnsColors.length != numberOfColumnsInGroup) {
            throw new Exception("Invalid number of number of colors in groups");
        }
        maxValue = calculateMaxValue(data);
        minValue = calculateMinValue(data);
        return true;
    }

    private void initColumnsPaints() {
        columnsPaints = new Paint[numberOfColumnsInGroup];
        for (int i = 0; i < numberOfColumnsInGroup; i++) {
            columnsPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            columnsPaints[i].setColor(columnsColors[i]);
            columnsPaints[i].setStyle(Paint.Style.FILL_AND_STROKE);
            columnsPaints[i].setStrokeWidth(columnWidth);
        }
    }

    private int[] calculateTitlesWidth(ArrayList<String> titles) {
        if ((null == titles) || (0 == titles.size())) {
            return null;
        }
        int[] retWidths = new int[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            retWidths[i] = (int) getStringWidth(titleTextPaint, titles.get(i));
        }
        return retWidths;
    }

    private int[] calculateValuesWidth(float maxValue, float minValue) {
        int[] retWidths = new int[2];
        retWidths[0] = (int) getStringWidth(valuesTextPaint, String.valueOf((int) maxValue));
        retWidths[1] = (int) getStringWidth(valuesTextPaint, String.valueOf((int) minValue));
        return retWidths;
    }

    private float calculateMaxValue(ArrayList<Float> data) {
        float theMax = Float.MIN_VALUE;
        for (int i = 0; i < data.size(); i++) {
            if (theMax < ((float) data.get(i))) {
                theMax = (float) data.get(i);
            }
        }
        return theMax;
    }

    private float calculateMinValue(ArrayList<Float> data) {
        float theMin = Float.MAX_VALUE;
        for (int i = 0; i < data.size(); i++) {
            if (theMin > ((float) data.get(i))) {
                theMin = (float) data.get(i);
            }
        }
        return theMin;
    }

    private int calculateColumnWidth(ArrayList data) {
        if (null == data || 0 == data.size()) {
            return MAX_COLUMN_WIDTH;
        }
        int groups = data.size() / numberOfColumnsInGroup;
        int theColumnWidth = width / (groups * 4);  // Green, Yellow, Red, Space
        return theColumnWidth > maxColumnWidth ? maxColumnWidth : theColumnWidth;
    }

    private float calculateMaxValueY(float graphHeight) {
        return ((graphHeight * 100) / 120) * (110F / 100);   // Leave top space which is 10% of the value
    }

    private float calculateMinValueY(float graphHeight) {
        return ((graphHeight * 100) / 120) * (10F / 100);   // Leave bottom space which is 10% of the value
    }

    private float calculateGraphHeight(int height, ArrayList<String> titles) {
        if (titles == null || titles.size() == 0) { // || true) {     // TODO
            return height - 1;
        }
        titleHeight = getStringHeight(titleTextPaint, titles.get(0));
        return (float) (height - (1.2 * titleHeight));
    }

    private int getStringHeight(Paint textPaint, String text) {
        Rect bound = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bound);
        return bound.height();
    }
}
