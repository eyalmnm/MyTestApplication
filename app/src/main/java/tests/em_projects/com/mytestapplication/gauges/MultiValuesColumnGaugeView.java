package tests.em_projects.com.mytestapplication.gauges;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by eyalmuchtar on 6/26/16.
 */

public class MultiValuesColumnGaugeView extends View {

    private static final String TAG = "MltValuesColumnGaugeVw";

    private static final int MAX_COLUMN_WIDTH = 20;

    // View properties
    private int width;
    private int height;
    private float graphHeight;
    private float graphStartX;
    private int titleHeight;

    // Graph's layout properties
    private Paint layoutLinesPaint;
    private int layoutLinesColor;
    private int layoutLinesWidth;
    private int valuesTextColor;
    private int titleTextColor;
    private Paint titleTextPaint;
    private Paint valuesTextPaint;
    private int[] titlesWidth;
    private int[] valuesWidth;


    // Data properties
    private Paint[] columnsPaints;
    private float maxValue;
    private float minValue;
    private int numberOfDataInGroup = 3;
    private int[] dataGroupColorsColumns = new int[]{Color.RED, Color.YELLOW, Color.GREEN};
    private int columnWidth = MAX_COLUMN_WIDTH;
    private float minValueY;
    private float maxValueY;

    // Data holders
    private ArrayList<Float> data;
    private ArrayList<String> titles;

    public MultiValuesColumnGaugeView(Context context) {
        super(context);

        init();
    }

    public MultiValuesColumnGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MultiValuesColumnGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
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

        columnsPaints = new Paint[numberOfDataInGroup];
        for (int i = 0; i < numberOfDataInGroup; i++) {
            columnsPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            columnsPaints[i].setColor(dataGroupColorsColumns[i]);
            columnsPaints[i].setStyle(Paint.Style.FILL_AND_STROKE);
            columnsPaints[i].setStrokeWidth(columnWidth);
        }
    }

    public void setData(ArrayList<Float> data, ArrayList<String> titles, int numberOfDataInGroup, int[] dataGroupColorsColumns) throws Exception {
        if (isValidData(data, titles, numberOfDataInGroup, dataGroupColorsColumns)) {
            maxValue = calculateMaxValue(data, numberOfDataInGroup);
            minValue = calculateMinValue(data, numberOfDataInGroup);

            this.data = data;
            this.titles = titles;
            this.numberOfDataInGroup = numberOfDataInGroup;
            this.dataGroupColorsColumns = dataGroupColorsColumns;

            columnWidth = calculateColumnWidth(data, width);

            initColumnsPaints(numberOfDataInGroup, dataGroupColorsColumns);

            invalidate();
        } else {
            throw new Exception("Invalid data. Please check your data validity.");
        }
    }

    private void initColumnsPaints(int numberOfDataInGroup, int[] dataGroupColorsColumns) {
        columnsPaints = new Paint[numberOfDataInGroup];
        for (int i = 0; i < numberOfDataInGroup; i++) {
            columnsPaints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            columnsPaints[i].setColor(dataGroupColorsColumns[i]);
            columnsPaints[i].setStyle(Paint.Style.FILL_AND_STROKE);
            columnsPaints[i].setStrokeWidth(columnWidth);
        }
    }

    private float calculateMaxValue(ArrayList<Float> data, int numberOfDataInGroup) {  // TODO Check this method
        float retValue = Float.MIN_VALUE;
        float sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
            if ((i % numberOfDataInGroup == (numberOfDataInGroup - 1))) {
                if (sum > retValue) {
                    retValue = sum;
                }
                sum = 0;
            }
        }
        return retValue;
    }

    private float calculateMinValue(ArrayList<Float> data, int numberOfDataInGroup) {  // TODO Check this method
        return  0F;
    }

    private boolean isValidData(ArrayList<Float> data, ArrayList<String> titles, int numberOfDataInGroup, int[] dataGroupColorsColumns) throws Exception {
        int groups = data.size() / numberOfDataInGroup;
        if (groups != titles.size()) {
            throw new Exception("Invalid number of titles or number of groups");
        }
        if (0 != (data.size() % numberOfDataInGroup)) {
            throw new Exception("Invalid number of number of groups");
        }
        if (dataGroupColorsColumns.length != numberOfDataInGroup) {
            throw new Exception("Invalid number of number of colors in groups");
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        columnWidth = calculateColumnWidth(data, width);

        titlesWidth = calculateTitlesWidth(titles);
        valuesWidth = calculateValuesWidth(maxValue, minValue);
        graphStartX = 1; // (float) (1.2 * Math.max(valuesWidth[0], valuesWidth[1])) + 1;  // TODO

        graphHeight = calculateGraphHeight(height, titles) - 1;
        maxValueY = calculateMaxValueY(graphHeight);
        minValueY = calculateMinValueY(graphHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGraphLayout(canvas);
        drawColumns(canvas);
        drawValues(canvas);
    }

    private int[] calculateTitlesWidth(ArrayList<String> titles) {
        if ((null == titles) || (0 == titles.size())) {
            return null;
        }
        int[] retWidths = new int[titles.size()];
        for (int i = 0; i < titles.size(); i++) {
            retWidths[i] = (int) ColumnsGaugeView.getStringWidth(titleTextPaint, titles.get(i));
        }
        return retWidths;
    }

    private int[] calculateValuesWidth(float maxValue, float minValue) {
        int[] retWidths = new int[2];
        retWidths[0] = (int) ColumnsGaugeView.getStringWidth(valuesTextPaint, String.valueOf((int) maxValue));
        retWidths[1] = (int) ColumnsGaugeView.getStringWidth(valuesTextPaint, String.valueOf((int) minValue));
        return retWidths;
    }

    private int calculateColumnWidth(ArrayList data, int width) {
        if (null == data || 0 == data.size() || width < MAX_COLUMN_WIDTH) {
            return MAX_COLUMN_WIDTH;
        }
        int groups = data.size() / numberOfDataInGroup;
        int theColumnWidth = (int) (width / (groups * 1.5));  // include Space
        return theColumnWidth > MAX_COLUMN_WIDTH ? MAX_COLUMN_WIDTH : theColumnWidth;
    }

    private void drawColumns(Canvas canvas) {
        int groupCounter = 0;
        int x = (int) graphStartX + layoutLinesWidth + 9;
        int y = (int) graphHeight - layoutLinesWidth - 8;
        float dataHeight = 0;
        for (int i = 0; i < data.size(); i++) {   // data.size()
            int modulo = i % numberOfDataInGroup;
            if ((0 < i) && (modulo == 0)) {
                x += columnWidth * 2.5F;
                y = (int) graphHeight - layoutLinesWidth - 8;
                drawTitle(canvas, x + 2, (int) (graphHeight + 11), titles.get(groupCounter++), valuesTextPaint);
            } else if (0 == i) {
                drawTitle(canvas, x + 2, (int) (graphHeight + 11), titles.get(groupCounter++), valuesTextPaint);
            }
            dataHeight = calculateDataHeight(data.get(i));
            drawGraphColumn(canvas, /*data.get(i),*/ x, y, dataHeight, /*modulo,*/ columnsPaints[modulo]);
            y -= dataHeight;
        }
    }

    private void drawTitle(Canvas canvas, int x, int y, String value, Paint valuesTextPaint) {
        Log.d(TAG, "drawTitle  x: " + x + " y: " + y + " value: " +  value);
        canvas.drawText(String.valueOf(value), x, y, valuesTextPaint);
    }

    private void drawGraphColumn(Canvas canvas, /*float value,*/ int x, int y, float dataHeight, /*int moduloIndex,*/ Paint columnsPaint) {
        int left = x;
        int top = (int) (y - dataHeight);
        int right = x + columnWidth;
        int bottom = y;
        Log.d(TAG, "drawGraphColumn  left: " + left + " top: " + top + " right: " +  right + " bottom: " + bottom + " dataHeight: " + dataHeight +
                "\n view height: " + height + " column width: " + columnWidth);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, columnsPaint);
//        canvas.save();
//        int xPivot = x - (8 * moduloIndex) - 10;   // TODO Calc text height
//        int yPivot = (bottom - 30);  // TODO Calc text width.
//        canvas.rotate(90, xPivot, yPivot);
//        canvas.drawText(String.valueOf(value), xPivot, yPivot - 30, valuesTextPaint);
//        canvas.restore();
    }

    private void drawValues(Canvas canvas) {
        int groupCounter = 0;
        int x = (int) graphStartX + layoutLinesWidth + 9;
        int y = (int) graphHeight - layoutLinesWidth - 8;
        float dataHeight = 0;
        for (int i = 0; i < data.size(); i++) {   // data.size()
            int modulo = i % numberOfDataInGroup;
            if ((0 < i) && (modulo == 0)) {
                x += columnWidth * 2.5F;
                y = (int) graphHeight - layoutLinesWidth - 8;
            }
            dataHeight = calculateDataHeight(data.get(i));
            int val = (int) data.get(i).floatValue();
            drawValue(canvas, x, y, modulo, String.valueOf(val));
            y -= dataHeight;
        }
    }
    private void drawValue(Canvas canvas, int x, int bottom, int moduloIndex, String value) {
        canvas.save();
        int xPivot = x - (10 * moduloIndex) - 9;   // TODO Calc text height
        int yPivot = (bottom - 20);  // TODO Calc text width.
        canvas.rotate(90, xPivot, yPivot);
        canvas.drawText(String.valueOf(value), xPivot, yPivot - 30, valuesTextPaint);
        canvas.restore();
    }

    private void drawGraphLayout(Canvas canvas) {
        Log.d(TAG, "drawGraphLayout graphHeight: " + graphHeight);
        canvas.drawLine(graphStartX, graphHeight, width, graphHeight, layoutLinesPaint);    // Horizontal
        canvas.drawLine(graphStartX, 0, graphStartX, graphHeight, layoutLinesPaint);        // Vertical
    }

    private int calculateDataHeight(float value) {
        float yDiff = maxValueY - minValueY;
        float valDiff = maxValue - minValue;
        return (int) ((((value - minValue) / valDiff) * yDiff)) ;  // + minValueY);
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
