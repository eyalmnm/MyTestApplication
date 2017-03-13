package tests.em_projects.com.mytestapplication.moving_BG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.ImageUtils;

/**
 * Created by eyalmuchtar on 6/2/16.
 */

public class MovingCroppedBGActivity extends Activity {

    private static final String TAG = "MovingCroppedBGActivity";

    private Bitmap orgImage;
    private View screenLayout;

    private Handler handler;
    private int counter = 0;
    private int xPos = 100;
    private int yPos = 100;
    private int step = 20;

    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_cropped_bg);
        screenLayout = findViewById(R.id.screenLayout);

        orgImage = BitmapFactory.decodeResource(getResources(), R.drawable.splash);

        handler = new Handler(getMainLooper());

        initBackground();

        startRunning();
    }

    private void initBackground() {
        screenLayout = (RelativeLayout) findViewById(R.id.screenLayout);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = (int) ImageUtils.dpFromPx(this, size.x);
        height = (int) ImageUtils.dpFromPx(this, size.y);
        xPos = calcXPosition();
        yPos = calcYPosition();
        setBgImage(orgImage, xPos, yPos, width, height);
    }

    private void startRunning() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               setBackGround();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void setBackGround() {
        counter = (++ counter) % 4;
        Log.d(TAG, "setBackGround counter: " + counter);
        switch (counter) {
            case 0:
                xPos += step;
                break;
            case 1:
                yPos += step;
                break;
            case 2:
                xPos -= step;
                break;
            case 3:
                yPos -= step;
        }
        setBgImage(orgImage, xPos, yPos, width, height);
    }

    private void setBgImage(Bitmap orgImage, int xPos, int yPos, int width, int height) {
        Bitmap bgImage = ImageUtils.imageCropper(orgImage, xPos, yPos, width, height);
        screenLayout.setBackgroundColor(0);
        screenLayout.setBackground(new BitmapDrawable(getResources(), bgImage));
    }

    // Calculate the the initialed Y position of background image.
    private int calcYPosition() {
        return (int) ((orgImage.getHeight() - height) / 2f);
    }

    // Calculate the the initialed X position of background image.
    private int calcXPosition() {
        return (int) ((orgImage.getWidth() - width) / 2F);
    }
}
