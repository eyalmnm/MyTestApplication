package tests.em_projects.com.mytestapplication.animations;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 23/04/16.
 */
// @Ref http://stackoverflow.com/questions/18147840/slide-right-to-left-android-animations
// @Ref http://stackoverflow.com/questions/31004146/view-is-not-visible-after-alpha0-animation
// @Ref http://www.androidhive.info/2013/06/android-working-with-xml-animations/

public class MovingButtons extends Activity {

    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_buttons);
        testButton = (Button) findViewById(R.id.testButton);

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.slide_in_buttom);
//        testButton.startAnimation(animation);
        testButton.animate().setDuration(3000);
        testButton.animate().translationX(-500); //.y(-100); // Right to left Animation
    }

    /*
        int xValue = container.getWidth() - animatingButton.getWidth();
       `int yValue = container.getHeight() - animatingButton.getHeight();
        animatingButton.animate().x(xValue).y(yValue);
     */
}
