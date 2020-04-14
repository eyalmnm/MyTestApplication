package tests.em_projects.com.mytestapplication.apartments.questionnaire;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatCheckBox;


public class SLCheckBox extends AppCompatCheckBox implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;

    public SLCheckBox(Context context, String questionId, String questionTitle, boolean isChecked) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;

        // Init the UI Component
        setChecked(isChecked);
        setText(questionTitle);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        setTypeface(boldTypeface);
    }

    @Override
    public String getQuestionId() {
        return questionId;
    }

    @Override
    public String getQuestionTitle() {
        return questionTitle;
    }

    @Override
    public String getQuestionAnswer() {
        return isChecked() ? "true" : "false";
    }
}
