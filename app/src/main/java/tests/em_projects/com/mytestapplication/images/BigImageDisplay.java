package tests.em_projects.com.mytestapplication.images;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 26/04/16.
 */
public class BigImageDisplay extends Activity {

    private ImageView bigImageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_dispaly);
    }
}
