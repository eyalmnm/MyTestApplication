package tests.em_projects.com.mytestapplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tests.em_projects.com.mytestapplication.frames_test.TwoFramesTestActivity;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;

/**
 * @ref http://stackoverflow.com/questions/3185103/how-to-define-a-circle-shape-in-an-android-xml-drawable-file
 * @ref http://stackoverflow.com/questions/15685485/android-shrink-and-grow-sequential-animation
 * @ref http://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
 * @ref http://stackoverflow.com/questions/28578701/create-android-shape-background-programmatically
 * @ref http://stackoverflow.com/questions/9387711/android-animation-flicker
 * @ref https://developer.android.com/google/play/billing/billing_integrate.html
 * @ref https://developer.android.com/training/in-app-billing/index.html
 * @ref https://developer.android.com/google/play/billing/index.html
 * <p>
 * TODO implement the following example
 * Add Permissions checking and requests
 * @ref https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition
 * @ref http://saulmm.github.io/mastering-coordinator
 * <p>
 * TODO Implement Local BroadcastReceiver for Piczaz
 * <p>
 * TODO Implement image picker for Orion
 * @ref github.com/luminousman/MultipleImagePick
 * TODO Playing with Retrofit
 * @ref http://square.github.io/retrofit/
 * @ref http://www.vogella.com/tutorials/Retrofit/article.html
 * @ref https://www.androidhive.info/2016/05/android-working-with-retrofit-http-library/ */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final static SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
    private ImageView circleShapeImage;
    private float minFactor = 1F;
    private float maxFactor = 1.2F;
    private final ScaleAnimation growAnim = new ScaleAnimation(minFactor, maxFactor, minFactor, maxFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
    private final ScaleAnimation shrinkAnim = new ScaleAnimation(maxFactor, minFactor, maxFactor, minFactor, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Resolution group is: " + (DimenUtils.dpToPx(160)) / 160, Toast.LENGTH_LONG).show();

        Log.d(TAG, "Now is: " + sdf.format(new Date(System.currentTimeMillis())));

        //playRingTones();
        //showRingTonePicker();

        circleShapeImage = (ImageView) findViewById(R.id.circleShapeImage);

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
                growAnim.start();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(MainActivity.this, PlotActivity.class);    // --> // TODO for Hera-Med
//                Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);   // --> // TODO for Hera-Med
//                Intent intent = new Intent(MainActivity.this, MovingButtons.class);
//                Intent intent = new Intent(MainActivity.this, CircularSeekBarActivity.class); // --> // TODO for Hera-Med
//                Intent intent = new Intent(MainActivity.this, EditablePrefList.class);
//                Intent intent = new Intent(MainActivity.this, DataUsageActivity.class);
//                Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);   // --> // TODO for Hera-Med
//                Intent intent = new Intent(MainActivity.this, CombinedAnimAndGauge.class);
//                Intent intent = new Intent(MainActivity.this, SimpleCarouselTest.class);
//                Intent intent = new Intent(MainActivity.this, StatusCounterActivity.class);    // --> // TODO for GreenRoad
//                Intent intent = new Intent(MainActivity.this, CustomViewActivity.class);  // --> // TODO for GreenRoad
//                Intent intent = new Intent(MainActivity.this, MovingCroppedBGActivity.class);
//                Intent intent = new Intent(MainActivity.this, BigImageDisplay.class);
//                Intent intent = new Intent(MainActivity.this, GrowBigImage.class);
//                Intent intent = new Intent(MainActivity.this, DeviceScanActivity.class);
//                Intent intent = new Intent(MainActivity.this, ChargingGaugeActivity.class); // --> // TODO for PowerMat
//                Intent intent = new Intent(MainActivity.this, PlaceTheRing.class);    // --> // TODO for PowerMat
//                Intent intent = new Intent(MainActivity.this, ArcGaugeActivity.class);    // --> // TODO for Integral
//                Intent intent = new Intent(MainActivity.this, ColumnsGaugeActivity.class);    // --> // TODO for Integral
//                Intent intent = new Intent(MainActivity.this, ActivityFragmentColumnsGauge.class);    // --> // TODO for Integral
//                Intent intent = new Intent(MainActivity.this, MultiValuesColumnGaugeActivity.class);  // --> // TODO for Integral
//                Intent intent = new Intent(MainActivity.this, MultiValuesCircleActivity.class);   // --> // TODO for Integral
//                Intent intent = new Intent(MainActivity.this, AutoStatusCounterActivity.class);     // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, ActivityRecognizedActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, BillingActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, AdjustResizeActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, TwoLinesNotificationViewActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, NewTwoLinesNotificationViewActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, IconWithBadgeActivity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, LayoutAnimTest.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, BatterySafeMode_Activity.class);    // --> // TODO for ListenApp
//                Intent intent = new Intent(MainActivity.this, ListViewMultipleSelectionActivity.class);    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, DirectoriesExplorerActivity.class);    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, AnimatedImageButton.class);    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, ApiCameraActivity.class);    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, JsonTestActivity.class);    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);    // --> // TODO for Orion
//                intent.putExtra("data", "");    // --> // TODO for Orion
//                Intent intent = new Intent(MainActivity.this, RetrofitDemoActivity.class);    // --> // TODO for StarLab
//                Intent intent = new Intent(MainActivity.this, MvvmGameActivity.class);    // --> // TODO for StarLab
                Intent intent = new Intent(MainActivity.this, TwoFramesTestActivity.class);    // --> // TODO for A-Studio
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void showRingTonePicker() {
        RingtonePickerDialog.Builder ringtonePickerBuilder =
                new RingtonePickerDialog.Builder(MainActivity.this, getSupportFragmentManager());

        //Set title of the dialog.
        //If set null, no title will be displayed.
        ringtonePickerBuilder.setTitle("Select ringtone");

        //Add the desirable ringtone types.
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);

        //set the text to display of the positive (ok) button. (Optional)
        ringtonePickerBuilder.setPositiveButtonText("SET RINGTONE");

        //set text to display as negative button. (Optional)
        ringtonePickerBuilder.setCancelButtonText("CANCEL");

        //Set flag true if you want to play the com.ringtonepicker.sample of the clicked tone.
        ringtonePickerBuilder.setPlaySampleWhileSelection(true);

        //Set the callback listener.
        ringtonePickerBuilder.setListener(new RingtonePickerListener() {
            @Override
            public void OnRingtoneSelected(String ringtoneName, Uri ringtoneUri) {
                Toast.makeText(MainActivity.this, "Ringtone Url: " + ringtoneUri.toString(), Toast.LENGTH_SHORT).show();
                playRingTones(ringtoneUri);
            }
        });

        //set the currently selected uri, to mark that ringtone as checked by default. (Optional)
        //ringtonePickerBuilder.setCurrentRingtoneUri(mCurrentSelectedUri);

        //Display the dialog.
        ringtonePickerBuilder.show();
    }

    private void playRingTones(Uri alert) {
        try {
            //Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                }, 100);
            }
        } catch (Exception e) {
        }
    }
}
