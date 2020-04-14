package tests.em_projects.com.mytestapplication.apartments

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_questionnaire.*
import tests.em_projects.com.mytestapplication.R
import tests.em_projects.com.mytestapplication.apartments.questionnaire.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class QuestionnaireActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val jsonStr: String? = loadRawQuestionnaire()
        if (jsonStr?.isNotEmpty() == true) {
            val questionnaireModel = getGson()?.fromJson(jsonStr, QuestionnaireModel::class.java)
            if (questionnaireModel != null) {
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val widgets = getWidgets(questionnaireModel)
                createQuestionnaire(container, widgets, params)
            }
        }
    }

    private fun createQuestionnaire(viewGroup: ViewGroup, widgets: List<SLWidgetInterface>, params: ViewGroup.LayoutParams) {
        for (widget: SLWidgetInterface in widgets) {
            viewGroup.addView(widget as View, params)
        }
    }

    private fun getWidgets(questionnaireModel: QuestionnaireModel): MutableList<SLWidgetInterface> {
        val widgets: MutableList<SLWidgetInterface> = mutableListOf()
        val questions = questionnaireModel.questions
        for (question: QuestionModel in questions) {
            val widget = widgetBuilder(question)
            if (widget != null) {
                widgets.add(widget)
            }
        }
        return widgets
    }

    private fun widgetBuilder(question: QuestionModel): SLWidgetInterface? {
        when (question.type) {
            Constants.TYPE_DATE_PICKER -> return SLDateTimeTextView(this, question.id, question.title, true)
            Constants.TYPE_CHECKBOX -> return SLCheckBoxGroup(this, question.id, question.title, question.answers)
            Constants.TYPE_LABEL -> return SLTextView(this, question.id, question.title)
            Constants.TYPE_NUMBER -> return SLEditText(this, question.id, question.title, InputType.TYPE_CLASS_PHONE)
            Constants.TYPE_RADIO -> return SLRadioGroup(this, question.id, question.title, question.answers, 0)
            Constants.TYPE_STRING -> return SLEditText(this, question.id, question.title)
            Constants.TYPE_SPINNER -> return SLSpinner(this, question.id, question.title, question.answers, 0)
            Constants.TYPE_TIME_PICKER -> return SLDateTimeTextView(this, question.id, question.title, false)
            else -> return null
        }
    }

    private fun getGson(): Gson? {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    private fun loadRawQuestionnaire(): String? {
        val inputStream = resources.openRawResource(R.raw.questionnaire)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var stringBuilder = StringBuilder()
        var line: String? = null
        try {
            while (true) {
                line = bufferedReader.readLine()  // ?: break // Test this option too
                if (line != null)
                    stringBuilder.append(line)
                else
                    break
            }
            return stringBuilder.toString()
        } catch (ex: IOException) {
            print("Exception $ex")
        } finally {
            bufferedReader.close()
            inputStream.close()
        }
        return null
    }
}