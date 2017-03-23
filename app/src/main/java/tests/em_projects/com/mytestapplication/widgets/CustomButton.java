package tests.em_projects.com.mytestapplication.widgets;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import tests.em_projects.com.mytestapplication.R;


public class CustomButton extends Button {
    public CustomButton(Context context, AttributeSet attrs) {
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
            AssetManager assetManager = context.getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/" + fontName + ".otf"); //".ttf");   // .otf
            setTypeface(typeface);
        }
    }
}
