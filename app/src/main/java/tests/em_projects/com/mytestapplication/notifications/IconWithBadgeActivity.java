package tests.em_projects.com.mytestapplication.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by USER on 06/06/2017.
 */

public class IconWithBadgeActivity extends Activity {

    private IconWithBadge iconWithBadge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_with_badge);

        iconWithBadge = (IconWithBadge) findViewById(R.id.iconWithBadge);

        iconWithBadge.setIcon(R.drawable.listenapp);

        iconWithBadge.setBadge(3);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                iconWithBadge.setBadge(0);
            }
        }, 2000);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                iconWithBadge.setIcon(R.drawable.speaker_on);
            }
        }, 4000);

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                iconWithBadge.setBadge(1);
            }
        }, 6000);

    }
}
