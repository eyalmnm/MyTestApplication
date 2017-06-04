package tests.em_projects.com.mytestapplication.notifications;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by USER on 04/06/2017.
 */

// @Ref: https://stackoverflow.com/questions/31308750/dragging-an-imagview-to-certain-position

public class NewTwoLinesNotificationViewService extends Service {
    private WindowManager windowManager;
    private NewTwoLinesNotificationView notView;
    private WindowManager.LayoutParams params;
    private int cor_x = 0;
    private int cor_y = 0;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("", "created");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        RelativeLayout container = new RelativeLayout(this);


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        notView = new NewTwoLinesNotificationView(this);
        notView.setTag("notView");

        RelativeLayout.LayoutParams chatHeadParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        chatHeadParams.addRule(Gravity.TOP | Gravity.LEFT);
        chatHeadParams.leftMargin = 300;
        chatHeadParams.topMargin = 300;

        container.addView(notView, chatHeadParams);
        windowManager.addView(container, params);

        container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                ClipData.Item item = new ClipData.Item(v.getTag().toString());


                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);

//                View.DragShadowBuilder myShadow = new MyDragShadowBuilder(notView);
                // Starts the drag
//                v.startDrag(dragData,  // the data to be dragged
//                        myShadow,      // the drag shadow builder
//                        null,          // no need to use local data
//                        0              // flags (not currently used, set to 0)
//                );

                return false;
            }

        });


        notView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                //cor_x = ((int) chatHead.getX());
                //cor_y = ((int) chatHead.getY());

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED: {
                        Log.d("", "x:" + event.getX() + "y:" + event.getY());
                        break;
                    }
                    case DragEvent.ACTION_DRAG_STARTED: {
                        Log.d("", "x:" + event.getX() + "  y:" + event.getY());
                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        cor_x = ((int) event.getX());
                        cor_y = ((int) event.getX());
                        Log.d("", "x:" + cor_x + "  y:" + cor_y);
                        break;
                    }
                    case DragEvent.ACTION_DRAG_EXITED: {
                        Log.d("", "x:" + cor_x + "  y:" + cor_y);
                        break;
                    }


                    case DragEvent.ACTION_DRAG_ENDED: {
                        if (windowManager != null && params != null) {
                            params.x = cor_x;
                            params.y = cor_y;
                            Log.d("", "x:" + cor_x + "  y:" + cor_y);
                            windowManager.removeView(notView);
                            windowManager.addView(notView, params);
                        }

                        return false;
                    }

                }


                return false;
            }
        });

        windowManager.addView(notView, params);
    }
}
