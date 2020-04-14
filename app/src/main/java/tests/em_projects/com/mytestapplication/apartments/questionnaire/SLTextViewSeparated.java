package tests.em_projects.com.mytestapplication.apartments.questionnaire;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

public class SLTextViewSeparated extends LinearLayout implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;
    private TextView textView;

    public SLTextViewSeparated(Context context, String questionId, String questionTitle) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;


        // Init Component
        setOrientation(LinearLayout.VERTICAL);

        int titleMarginTop = getResources().getDimensionPixelSize(R.dimen.question_titles_margin_top);
        int contentMarginTop = getResources().getDimensionPixelSize(R.dimen.question_content_margin_top);
        int contentMarginLeft = getResources().getDimensionPixelSize(R.dimen.question_content_margin_left);
        int textPaddingH = getResources().getDimensionPixelSize(R.dimen.question_text_padding_h);
        int textPaddingV = getResources().getDimensionPixelSize(R.dimen.question_text_padding_v);

        LayoutParams tvParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(contentMarginLeft, titleMarginTop, 0, 0);

        textView = new TextView(context);
        textView.setText(questionTitle);
        textView.setTextColor(getResources().getColor(R.color.question_title));
        textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.question_title_text_size));
        if (false == StringUtils.isNullOrEmpty(questionTitle)) {
            addView(textView, tvParams);
        }
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
