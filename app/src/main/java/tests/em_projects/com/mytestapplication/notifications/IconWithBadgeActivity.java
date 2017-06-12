package tests.em_projects.com.mytestapplication.notifications;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;

/**
 * Created by USER on 06/06/2017.
 */

public class IconWithBadgeActivity extends Activity {

    private IconWithBadge iconWithBadge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
//        setContentView(R.layout.activity_icon_with_badge);
//        iconWithBadge = (IconWithBadge) findViewById(R.id.iconWithBadge);

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

    private View getView() {
        LinearLayout mainView = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.setOrientation(LinearLayout.HORIZONTAL);
        mainView.setLayoutParams(layoutParams);
        int padding = DimenUtils.dpToPx(5);
        mainView.setPadding(padding, padding, padding, padding);
        mainView.setBackgroundColor(Color.YELLOW);

        iconWithBadge = new IconWithBadge(this);
        int dim = DimenUtils.dpToPx(40);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dim, dim);
        mainView.addView(iconWithBadge, iconParams);

        return mainView;
    }
}
