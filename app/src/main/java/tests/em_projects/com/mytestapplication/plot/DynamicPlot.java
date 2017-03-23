package tests.em_projects.com.mytestapplication.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Vector;

/**
 * @ref http://code.tutsplus.com/tutorials/android-sdk-creating-custom-views--mobile-14548
 * @ref http://stackoverflow.com/questions/1963686/how-do-you-set-a-lines-width-when-drawing-in-android
 * @ref http://stackoverflow.com/questions/7178104/android-draw-line-with-width
 * @ref http://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
 */

/**
 * Created by Eyal.Muchtar on 10/04/2016.
 */
public class DynamicPlot extends View {

    private static final String TAG = "DynamicPlot";

    private static final int STEP_TIME = 10; // 0.01 Sec

    private int mWidth = 0;
    private int mHeight= 0;
    private int mSpaceBetweenVerticals = 0;
    private Vector<Integer> mVerticalsPositions = new Vector<>(5, 5);
    private Vector<Integer> mHorizontalsPositions = new Vector<>(5, 5);

    // View's attributes.
    private int mBgColor = 0xFFFFFFFF;
    private int mVerticalColor = 0xFF006600;
    private int mVerticalWidth = 3;
    private int mMinVertical = 5;
    private float mMinHorizontalsVal = 10;
    private float mMaxHorizontalsVal = 50;
    private float mHorizontalsStep = 10;
    private int mHorizontalColor = 0xFF660000;
    private int mHorizontalWidth = 3;
    private long mDisplayDurationSecond = 300;   // Time range of the screen
    private long mFramesPerSecond = 100;

    private Paint mBgPaint = null;
    private Paint mVerticalsPaint = null;
    private Paint mHorizontalsPaint = null;
	
	// Time frame
	private long startTime;
	private long timeRange;
    private long time_step = STEP_TIME;
    private int width_step;

    // Plot animation properties
    private volatile boolean mRunAnimation = false;
    private Runnable DecrementTask = new Runnable() {
        @Override
        public void run() {
            checkVerticalsPositionValidation();
            checkHorizontalsPositionValidation();
            invalidate();
            postDelayed(this, STEP_TIME);
        }
    };

    public DynamicPlot(Context context) {
        super(context);
        init();
    }

    public DynamicPlot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicPlot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRunAnimation = false;
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        mVerticalsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHorizontalsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        calculateMovingStepAndTimeStep();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateMovingStepAndTimeStep();
    }

    private void calculateMovingStepAndTimeStep() {   // TODO This is important calculation
        if (mWidth <= 0) {
            mWidth = getMeasuredWidth();
        }
        float secWidth = (float) (mWidth / (mDisplayDurationSecond * 1F));
        width_step = (int) (mWidth / (secWidth * mFramesPerSecond * 1F));
        time_step = (long) (1000 / (mFramesPerSecond * 1F));

        Log.d(TAG, "calculateMovingStepAndTimeStep secWidth: " + secWidth + " width_step: " + width_step + " time_step: " + time_step);
    }

    private void calculateSpacesBetweenVerticals() {   // TODO This is important calculation
        if (mWidth <= 0) {
            mWidth = getMeasuredWidth();
        }
        mSpaceBetweenVerticals = (mWidth - mMinVertical * mVerticalWidth) / mMinVertical;
        for (int i = 0; i < mMinVertical; i ++) {
            mVerticalsPositions.add((mSpaceBetweenVerticals + mVerticalWidth) * i);
        }
    }

    private void calculateHorizontalsPositions() {
        int numOfHorizontals = (int) ((mMaxHorizontalsVal - mMinHorizontalsVal) / mHorizontalsStep); // + 1;
        mHorizontalsPositions = new Vector<>();
        if (numOfHorizontals > 1) {
            if (mHeight <= 0) {
                mHeight = getMeasuredHeight();
            }
            int spaceBetweenHorizontals = (mHeight - numOfHorizontals * mHorizontalWidth) / numOfHorizontals;
            for (int i = 0; i < numOfHorizontals; i ++) {
                mHorizontalsPositions.add((spaceBetweenHorizontals + mHorizontalWidth) * i);
            }
        }
    }

    public void start() {
        postDelayed(DecrementTask, STEP_TIME);
    }

    public void stop() {
        removeCallbacks(DecrementTask);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth <= 0) {
            mWidth = getMeasuredWidth();
        }
        if (mHeight <= 0) {
            mHeight = getMeasuredHeight();
        }

        if (mSpaceBetweenVerticals <= 0) {
            calculateSpacesBetweenVerticals();
        }

        if (mHorizontalsPositions == null || mHorizontalsPositions.size() == 0) {
            calculateHorizontalsPositions();
        }

        if (mBgPaint != null) {
            mBgPaint.setColor(mBgColor);
            canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);
        }

        if (mHorizontalsPositions != null && mHorizontalsPositions.size() > 1) {
            mHorizontalsPaint.setColor(mHorizontalColor);
            mHorizontalsPaint.setStrokeWidth(mHorizontalWidth);
            for (int i = 0; i < mHorizontalsPositions.size(); i ++) {
                int yPos = mHorizontalsPositions.get(i);
                canvas.drawLine(0, yPos, mWidth, yPos, mHorizontalsPaint);
            }
        }

        if (mVerticalsPositions != null && mVerticalsPositions.size() > 0) {
            mVerticalsPaint.setColor(mVerticalColor);
            mVerticalsPaint.setStrokeWidth(mVerticalWidth);
            for (int i = 0; i < mVerticalsPositions.size(); i ++) {
                int xPos = mVerticalsPositions.get(i);
                canvas.drawLine(xPos, 0, xPos, mHeight, mVerticalsPaint);
//                Log.d(TAG, "xPos: " + xPos);
                xPos -= width_step;
                mVerticalsPositions.set(i, xPos);
            }
        }
    }

    private void checkVerticalsPositionValidation() {
        for (int i = 0; i < mVerticalsPositions.size(); i ++) {
            int xPos = mVerticalsPositions.get(i);
            if (xPos < (mVerticalWidth * -1)) {
                mVerticalsPositions.remove(i);
            }
        }
        int lastVerticalPosition = 0;
        if(mVerticalsPositions.size() > 0) {
            lastVerticalPosition = mVerticalsPositions.get(mVerticalsPositions.size() - 1) + mVerticalWidth;
        }
        if ((lastVerticalPosition + mSpaceBetweenVerticals) < mWidth) {
            mVerticalsPositions.add(lastVerticalPosition + mSpaceBetweenVerticals);
        }
    }

    private void checkHorizontalsPositionValidation() {
        // TODO do nothing currently
    }
}
