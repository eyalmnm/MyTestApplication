package tests.em_projects.com.mytestapplication.plot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import tests.em_projects.com.mytestapplication.R
import tests.em_projects.com.mytestapplication.utils.DimenUtils
import java.util.*
import kotlin.math.abs

// Ref: https://developer.android.com/training/custom-views/create-view

class ColumnsPlot(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val TAG = "ColumnsPlot"

    // View Properties
    private var width: Int? = null
    private var height: Int? = null

    // Plot Properties
    private val values: LinkedList<Float> = LinkedList()
    private val layoutColor: Int = Color.BLUE
    private val layoutLineWidth: Float = DimenUtils.dpToPx(5)
    private val posValueColor: Int = Color.GREEN
    private val negValueColor: Int = Color.RED
    private val corStrokeDim: Float = DimenUtils.dpToPx(8)
    private var verticalAxisHeight: Float = 0F

    // Layout Properties
    private lateinit var layoutLinesPaint: Paint
    private lateinit var columnsPaintsPositive: Paint
    private lateinit var columnsPaintsNegaitive: Paint
    private lateinit var valuesTextPaint: Paint
    private var valuesTextColor: Int = 0

    // Attributes values
    private var maxColumns: Int? = null
    private var maxValue: Float? = null
    private var minValue: Float? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ColumnsPlot, 0, 0).apply {
            try {
                maxColumns = 10
                maxValue = getFloat(R.styleable.ColumnsPlot_maxValue, 100F)
                minValue = getFloat(R.styleable.ColumnsPlot_minValue, 0F)
            } finally {
                recycle()
            }
        }

        // Value Text Paint
        valuesTextColor = Color.BLACK
        valuesTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        valuesTextPaint.color = valuesTextColor

        // Init Layout Paint
        layoutLinesPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        layoutLinesPaint.color = layoutColor
        layoutLinesPaint.style = Paint.Style.FILL_AND_STROKE
        layoutLinesPaint.strokeWidth = layoutLineWidth //corStrokeDim

        columnsPaintsPositive = Paint(Paint.ANTI_ALIAS_FLAG)
        columnsPaintsPositive.color = posValueColor
        columnsPaintsPositive.style = Paint.Style.FILL_AND_STROKE
        columnsPaintsPositive.strokeWidth = corStrokeDim

        columnsPaintsNegaitive = Paint(Paint.ANTI_ALIAS_FLAG)
        columnsPaintsNegaitive.color = negValueColor
        columnsPaintsNegaitive.style = Paint.Style.FILL_AND_STROKE
        columnsPaintsNegaitive.strokeWidth = corStrokeDim
    }

    fun addValue(value: Float) {
        values.add(value)

        if (values.size > maxColumns!!) {
            values.poll()
        }

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        width = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)

        verticalAxisHeight = calculateVerticalAxisHeight()
        maxColumns = ((width!! / (corStrokeDim * 2.2F)) - 1).toInt()

        invalidate()
    }

    private fun calculateVerticalAxisHeight(): Float {
        if (minValue == 0F) return height!!.toFloat()

        val total = abs(maxValue!!) + abs(minValue!!)
        val factor = height!! / total

        return factor * Math.abs(maxValue!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Draw Vertical and Horizontal axis lines
        drawLayoutAxisLines(canvas)

        // Draw Columns lines
        drawColumns(canvas)
    }

    private fun drawLayoutAxisLines(canvas: Canvas?) {
        canvas!!.drawLine(1F, 1f, 1F, ((height ?: 1) - 1).toFloat(), layoutLinesPaint) // Vertical
        canvas.drawLine(1F, verticalAxisHeight, width!!.toFloat(), verticalAxisHeight, layoutLinesPaint) // Horizontal
    }

    private fun drawColumns(canvas: Canvas?) {
        var x = layoutLineWidth + 5
        for (i in 0 until values.size) {
            if (values[i] > 0F) {
                drawGraphColumn(canvas, values[i], x.toInt(), columnsPaintsPositive)
            } else {
                drawGraphColumn(canvas, values[i], x.toInt(), columnsPaintsNegaitive)
            }
            x += corStrokeDim * 2.2F
        }
    }

    private fun drawGraphColumn(canvas: Canvas?, value: Float, x: Int, currentColumnPaint: Paint) {
        val y: Int = calculateYValue(value).toInt()
        Log.d(TAG, "y: $y   top: ${verticalAxisHeight.toInt() - y}   bottom: $verticalAxisHeight")
        val factor: Float = layoutLineWidth * 1.4F
        Log.d(TAG, "factor: $factor")
        var rect: Rect? = null
        var yPivot: Int? = 0
        if (value > 0) {
            rect = Rect(x, verticalAxisHeight.toInt() - y, x + corStrokeDim.toInt(), verticalAxisHeight.toInt() - factor.toInt())
            yPivot = (height!! * 1 / 4).toInt() // + verticalAxisHeight
        } else {
            rect = Rect(x, verticalAxisHeight.toInt() + factor.toInt(), x + corStrokeDim.toInt(), verticalAxisHeight.toInt() - y)
            yPivot = (height!! * 3 / 4).toInt() // + verticalAxisHeight
        }
        canvas?.drawRect(rect, currentColumnPaint)
        canvas?.save()
        val xPivot = x * 1
        Log.d(TAG, "xPivot: $xPivot   yPivot: $yPivot")
        canvas?.rotate(90f, xPivot.toFloat(), yPivot.toFloat())
        canvas?.drawText(value.toString(), xPivot.toFloat(), yPivot.toFloat() - 2, valuesTextPaint)
        canvas?.restore()
    }

    private fun calculateYValue(value: Float): Float {
        return (value / (abs(maxValue!!) + abs(minValue!!))) * height!!
    }
}