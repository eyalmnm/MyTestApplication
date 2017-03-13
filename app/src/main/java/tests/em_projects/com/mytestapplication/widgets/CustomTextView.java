package tests.em_projects.com.mytestapplication.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;


public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Typeface.createFromAsset doesn't work in the layout editor.
        // Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = styledAttrs.getString(R.styleable.CustomTextView_custom_font);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".oft"); // ".ttf");   // .oft
            setTypeface(typeface);
        }
    }
}
