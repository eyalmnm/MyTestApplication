package tests.em_projects.com.mytestapplication.animations;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import androidx.annotation.Nullable;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 22/10/2017.
 */

// Ref: https://stackoverflow.com/questions/33915142/android-rotate-image-inside-the-button

public class AnimatedImageButton extends Activity implements View.OnClickListener {

    private Button testButton;
    private boolean tuggleButton = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_image_button);

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float deg = (testButton.getRotation() == 180F) ? 0F : 180F; // testButton.getRotation() + 180F;
                testButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });
    }
}
