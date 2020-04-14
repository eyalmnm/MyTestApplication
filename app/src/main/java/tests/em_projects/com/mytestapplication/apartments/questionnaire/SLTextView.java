package tests.em_projects.com.mytestapplication.apartments.questionnaire;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;

public class SLTextView extends AppCompatTextView implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;

    public SLTextView(Context context, String questionId, String questionTitle) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        setText(questionTitle);
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
        return null;
    }
}
