package tests.em_projects.com.mytestapplication.notifications;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

/**
 * Created by USER on 06/06/2017.
 */

public class IconWithBadge extends RelativeLayout {
    private static final String TAG = "IconWithBadge";

    // View Components
    private int padding;
    private ImageView iconImageView;
    private int iconImageViewId;
    private TextView badgeTextView;


    public IconWithBadge(Context context) {
        super(context);
        initView();
    }

    public IconWithBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IconWithBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        padding = DimenUtils.dpToPx(5);
        this.setPadding(padding, padding, padding, padding);

        LayoutParams iconParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iconParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        int iconMargin = DimenUtils.dpToPx(1);
        iconParams.setMargins(iconMargin, iconMargin, iconMargin, iconMargin);
        iconImageView = new ImageView(getContext());
        iconImageViewId = View.generateViewId();
        iconImageView.setId(iconImageViewId);

        LayoutParams badgeParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        badgeParams.addRule(RelativeLayout.ALIGN_BOTTOM, iconImageViewId);
        badgeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        badgeTextView = new TextView(getContext());
        badgeTextView.setBackground(getContext().getResources().getDrawable(R.drawable.icon_badge));
        badgeTextView.setVisibility(INVISIBLE);

        addView(iconImageView, iconParams);
        addView(badgeTextView, badgeParams);
    }

    public boolean setIcon(int iconResId) {
        iconImageView.setImageResource(iconResId);
        return true;
    }

    public boolean setBadge(int number) {
        if (0 == number) {
            badgeTextView.setVisibility(INVISIBLE);
        } else {
            badgeTextView.setVisibility(VISIBLE);
            badgeTextView.setText(String.valueOf(number));
        }
        return true;
    }

    public int getBadgeNumber() {
        String numberStr = badgeTextView.getText().toString();
        if (true == StringUtils.isNullOrEmpty(numberStr)) {
            return 0;
        } else {
            return Integer.parseInt(numberStr);
        }
    }
}
