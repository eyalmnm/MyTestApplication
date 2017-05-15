package tests.em_projects.com.mytestapplication.camera;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;

/**
 * Created by USER on 08/05/2017.
 */

public class CameraDetection implements ICamera {

    private boolean isCameraOpened = false;
    private Handler handler;

    public CameraDetection(Context context) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handler = new Handler();
            manager.registerAvailabilityCallback(new CameraManager.AvailabilityCallback() {
                @Override
                public void onCameraAvailable(String cameraId) {
                    super.onCameraAvailable(cameraId);
                    isCameraOpened = false;
                }

                @Override
                public void onCameraUnavailable(String cameraId) {
                    super.onCameraUnavailable(cameraId);
                    isCameraOpened = true;
                }
            }, handler);
        }
    }

    @Override
    public boolean isOpen() {
        return isCameraOpened;
    }
}
