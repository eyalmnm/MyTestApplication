package tests.em_projects.com.mytestapplication.layout_anim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal muchtar on 12/07/2017.
 */

public class LayoutAnimTest extends Activity implements View.OnClickListener {

    private Button showHideButton;
    private LinearLayout theLayOut;

    private Animation showAnimation;
    private Animation hideAnimation;

    private boolean isShow = false;
    private float layoutHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_anim_test);

        showHideButton = (Button) findViewById(R.id.showHideButton);
        showHideButton.setOnClickListener(this);
        theLayOut = (LinearLayout) findViewById(R.id.theLayOut);

    }

    @Override
    protected void onResume() {
        super.onResume();
        theLayOut.post(new Runnable() {
            @Override
            public void run() {
                layoutHeight = theLayOut.getMeasuredHeight();
                theLayOut.setVisibility(View.GONE);
                showAnimation = new TranslateAnimation(0F, 0F, layoutHeight, 0F); // -theLayOut.getMeasuredHeight(), 0f);
                hideAnimation = new TranslateAnimation(0F, 0F, 0F, layoutHeight); // -theLayOut.getMeasuredHeight());
                showAnimation.setDuration(1000);
                hideAnimation.setDuration(1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (false == theLayOut.isShown()) {
            theLayOut.setVisibility(View.VISIBLE);
            theLayOut.startAnimation(showAnimation);
            showHideButton.setText("Hide");
        } else {
            theLayOut.startAnimation(hideAnimation);
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    theLayOut.setVisibility(View.GONE);
                    showHideButton.setText("Show");
                }
            }, hideAnimation.getDuration());
        }

    }
}
