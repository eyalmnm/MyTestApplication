package tests.em_projects.com.mytestapplication.notifications;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;

/**
 * Created by Eyal Muchtar on 18/05/2017.
 */

public class TwoLinesNotificationView extends LinearLayout {
    private static final String TAG = "TwoLinesNotificationVw";

    private static final long DISPLAY_DURATION = 3000;    // milliseconds
    private static final int STEP_TIME = 1000;
    private final int TICK_WHAT = 2;
    private ArrayList<NotificationIconView> iconViews;
    // Views properties
    private LinearLayout iconsLayout;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (STEP_TIME > 0) {
                updateNotificationIcons();
                invalidate();
            }
            postDelayed(this, STEP_TIME);
        }
    };
    private int padding = 0;
    // Notification View Components
    private ImageView avatar;
    private TextView message;
    private ImageView notification_icon;
    private ImageView app_icon;

    public TwoLinesNotificationView(Context context) {
        super(context);
        initView(context);
    }

    public TwoLinesNotificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TwoLinesNotificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void updateNotificationIcons() {
        long now = System.currentTimeMillis();
        ImageView imageView = null;
        for (int i = 0; i < iconViews.size(); i++) {
            if ((now - iconViews.get(i).getTimeStamp()) >= DISPLAY_DURATION) {
                imageView = (ImageView) iconViews.get(i).getView();
                AnimationSet animationSet = iconOutAnimation();
                imageView.setAnimation(animationSet);
                animationSet.start();
                iconViews.remove(i);
                iconsLayout.removeView(imageView);
            }
        }
        invalidate();
    }

    private void initView(Context context) {
        this.setOrientation(VERTICAL);
        padding = DimenUtils.dpToPx(5);
        this.setPadding(padding, padding, padding, padding);

        iconViews = new ArrayList<>();

        // Inquire the notification View
        ViewGroup notificationsView = (ViewGroup) View.inflate(context, R.layout.notification, null);
        initNotificationsViewComponents(notificationsView);

        // Create the Icon's layout
        iconsLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DimenUtils.dpToPx(50));
        iconsLayout.setLayoutParams(layoutParams);
        iconsLayout.setGravity(Gravity.RIGHT);

        // Add Views to root
        addView(notificationsView);
        addView(iconsLayout);

        start();
    }

    private void initNotificationsViewComponents(ViewGroup notificationsView) {
        avatar = (ImageView) notificationsView.findViewById(R.id.avatar);
        message = (TextView) notificationsView.findViewById(R.id.message);
        notification_icon = (ImageView) notificationsView.findViewById(R.id.notification_icon);
        app_icon = (ImageView) notificationsView.findViewById(R.id.app_icon);
    }

    public boolean showNotification(int avatarResId, String msg, int notificationIconResId, int appIconResId) {
        avatar.setImageResource(avatarResId);
        message.setText(msg);
        notification_icon.setImageResource(notificationIconResId);
        app_icon.setImageResource(appIconResId == 0 ? R.drawable.icon : appIconResId);
        invalidate();
        return true;
    }

    public boolean addIcon(int iconResId) {
        LayoutParams params = new LayoutParams(
                DimenUtils.dpToPx(40),
                DimenUtils.dpToPx(40)
        );
        params.setMargins(0, padding, padding, padding);
        ImageView iconImageView = new ImageView(getContext());
        iconImageView.setImageResource(iconResId);
        iconImageView.setLayoutParams(params);
        // Animation
        AnimationSet animationSet = iconInAnimation();
        iconImageView.setAnimation(animationSet);
        animationSet.start();
        iconViews.add(new NotificationIconView(iconImageView, System.currentTimeMillis()));
        iconsLayout.addView(iconImageView);
        invalidate();
        return true;
    }

    public void start() {
        postDelayed(timerRunnable, STEP_TIME);
    }

    public void stop() {
        removeCallbacks(timerRunnable);
    }

    private AnimationSet iconInAnimation() {
        AnimationSet animationSet;
        Log.d(TAG, "cell In Animation");
        animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(200);
        animationSet.addAnimation(animation);
        return animationSet;
    }

    private AnimationSet iconOutAnimation() {
        AnimationSet animationSet;
        Log.d(TAG, "cell Out Animation");
        animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(100);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(200);
        animationSet.addAnimation(animation);
        return animationSet;
    }


    private class NotificationIconView {

        private View view;
        private long timeStamp;

        public NotificationIconView(View view, long timeStamp) {
            this.view = view;
            this.timeStamp = timeStamp;
        }

        public View getView() {
            return view;
        }

        public long getTimeStamp() {
            return timeStamp;
        }
    }
}
