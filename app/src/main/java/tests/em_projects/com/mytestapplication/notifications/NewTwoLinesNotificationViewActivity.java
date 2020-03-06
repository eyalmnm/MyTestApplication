package tests.em_projects.com.mytestapplication.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import tests.em_projects.com.mytestapplication.R;

/**
 * private final int TICK_WHAT = 2;
 * Created by USER on 18/05/2017.
 */

public class NewTwoLinesNotificationViewActivity extends Activity {

    private static final int TICK_WHAT = 2;
    private final long frequency = 1500;    // milliseconds
    int counter = 0;
    private NewTwoLinesNotificationView view;
    private Handler handler;
    private volatile boolean isRunning = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_two_lines_notification);

        view = findViewById(R.id.newTwoLinesNotificationView);

        initServiceHandler();
    }

    private void initServiceHandler() {
        isRunning = true;
        handler = new Handler(getMainLooper()) {
            public void handleMessage(Message m) {
                updateNotification();
                if (true == isRunning) {
                    sendMessageDelayed(Message.obtain(this, TICK_WHAT), frequency);
                }
            }
        };
        handler.sendMessageDelayed(Message.obtain(handler, TICK_WHAT), frequency);
    }

    private void updateNotification() {
        view.addIcon(R.drawable.speaker_on);
        if (3 < counter++) {
            isRunning = false;
        }
        view.addNotification();
        if (0 == (counter % 2)) {
            view.refreshNotification();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeMessages(TICK_WHAT);
            handler = null;
        }
    }
}
