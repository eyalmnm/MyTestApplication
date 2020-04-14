package tests.em_projects.com.mytestapplication.plot

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_columns_plot.*
import tests.em_projects.com.mytestapplication.R

class ColumnPlotActivity : AppCompatActivity() {
    private val TAG: String = "ColumnPlotActivity"
    private val DELAY_INTERVAL = 125

    private var theValue = 0
    private var positive = true

    private var handler: Handler = Handler()
    var updateCounter: Runnable = object : Runnable {
        override fun run() {
            addValue()
            handler.postDelayed(this, DELAY_INTERVAL.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_columns_plot)
        Log.d(TAG, "onCreate")

        handler.postDelayed(updateCounter, DELAY_INTERVAL.toLong())
    }

    private fun addValue() {
        theValue = (theValue + 10) % 100
        val factor = if (positive) 1F else -1F
        columnPlot.addValue(theValue.toFloat() * factor)
        positive = !positive
    }
}