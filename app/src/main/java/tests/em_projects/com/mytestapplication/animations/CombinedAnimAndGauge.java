package tests.em_projects.com.mytestapplication.animations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.gauges.CircularSeekBar;
import tests.em_projects.com.mytestapplication.viewPage.ViewPagerActivity;

/**
 * Created by eyal on 28/04/16.
 */
public class CombinedAnimAndGauge extends Activity {

    private static final String TAG = "CombinedAnimAndGauge";

    private ImageView circleShapeImage;
    private CircularSeekBar circular;

    private int counter = 0;

    private final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
    private final ScaleAnimation shrinkAnim = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.combined_anim_and_gauge);

        circleShapeImage = (ImageView) findViewById(R.id.circleShapeImage);
        circular = (CircularSeekBar) findViewById(R.id.circular);

        growAnim.setDuration(2000);
        shrinkAnim.setDuration(2000);

        circleShapeImage.setAnimation(growAnim);
        growAnim.start();

        growAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circleShapeImage.setAnimation(shrinkAnim);
                shrinkAnim.start();
            }
        });
        shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circleShapeImage.setAnimation(growAnim);
                counter += 5;
                counter = counter % 100;
                circular.setTextPercents(counter);
                growAnim.start();
            }
        });
    }
}
