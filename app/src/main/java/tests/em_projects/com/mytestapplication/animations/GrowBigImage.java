package tests.em_projects.com.mytestapplication.animations;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/4/16.
 */

public class GrowBigImage extends Activity {

    private final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);

    private ImageView bigImageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        

        setContentView(R.layout.activity_grow_big_image);

        growAnim.setDuration(2000);
        bigImageImageView = (ImageView) findViewById(R.id.bigImageImageView);
//        bigImageImageView.setScaleX(0F);
//        bigImageImageView.setScaleY(0F);

        bigImageImageView.setAnimation(growAnim);
        growAnim.start();
    }
}
