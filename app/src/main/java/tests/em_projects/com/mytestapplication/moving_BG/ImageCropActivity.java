package tests.em_projects.com.mytestapplication.moving_BG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import tests.em_projects.com.mytestapplication.R;

/**
 * @ref http://stackoverflow.com/questions/9747295/crop-particular-part-of-image-in-android
 * @ref http://stackoverflow.com/questions/4605527/converting-pixels-to-dp
 * @ref http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
 * @ref http://stackoverflow.com/questions/22005952/how-to-get-screen-width-dpi-in-android
 * @ref http://stackoverflow.com/questions/22603153/how-to-programmatically-change-the-background-image-of-an-android-activity
 * @ref http://stackoverflow.com/questions/2415619/how-to-convert-a-bitmap-to-drawable-in-android
 * <p/>
 * Created by E M on 15/04/2016.
 */
public class ImageCropActivity extends Activity {

    private static final String TAG = "ImageCropActivity";

    private ImageView imageCropImageView;
    private Bitmap bigBitmap;

    private int width = 0;
    private int height = 0;

    private int x = 0;
    private int y = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            upadateImage();
            sendMessageDelayed(Message.obtain(this, 100), 100);
        }
    };

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_image_crop);

        imageCropImageView = (ImageView) findViewById(R.id.imageCropImageView);

        bigBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        Log.d(TAG, "imageHeight: " + bigBitmap.getHeight());
        Log.d(TAG, "imageWidth: " + bigBitmap.getWidth());
        Log.d(TAG, "imageHeight dpFromPx: " + dpFromPx(this, bigBitmap.getHeight()));
        Log.d(TAG, "imageWidth dpFromPx: " + dpFromPx(this, bigBitmap.getWidth()));
        Log.d(TAG, "imageHeight pxFromDp: " + pxFromDp(this, bigBitmap.getHeight()));
        Log.d(TAG, "imageWidth pxFromDp: " + pxFromDp(this, bigBitmap.getWidth()));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthPx = size.x;
        int heightPx = size.y;
        Log.d(TAG, "Screen height: " + heightPx + " pixels");
        Log.d(TAG, "Screen width: " + widthPx + " pixels");
        width = (int) pxFromDp(this, widthPx);
        height = (int) pxFromDp(this, heightPx);
        Log.d(TAG, "Screen height: " + height + " DP");
        Log.d(TAG, "Screen width: " + width + " DP");

//        Bitmap croppedBitmap = cropAndGivePointedShape(bigBitmap);
        Bitmap croppedBitmap = Bitmap.createBitmap(bigBitmap, 500, 800, 200, 200);
        imageCropImageView.setImageBitmap(croppedBitmap);

        mHandler.sendMessageDelayed(Message.obtain(mHandler, 100), 100);
    }

    private void upadateImage() {
        x += 50;
        y += 50;
        Bitmap croppedBitmap = Bitmap.createBitmap(bigBitmap, x, y, 200, 200);
        imageCropImageView.setImageBitmap(croppedBitmap);
    }

    private Bitmap cropAndGivePointedShape(Bitmap originalBitmap) {
        Bitmap bmOverlay = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        canvas.drawRect(0, 0, 20, 20, p);

        Point a = new Point(0, 20);
        Point b = new Point(20, 20);
        Point c = new Point(0, 40);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);

        a = new Point(0, 40);
        b = new Point(0, 60);
        c = new Point(20, 60);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);

        canvas.drawRect(0, 60, 20, originalBitmap.getHeight(), p);

        return bmOverlay;
    }
}
