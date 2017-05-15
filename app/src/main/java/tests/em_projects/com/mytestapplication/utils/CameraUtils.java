package tests.em_projects.com.mytestapplication.utils;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import tests.em_projects.com.mytestapplication.camera.CameraDetection;
import tests.em_projects.com.mytestapplication.camera.ICamera;
import tests.em_projects.com.mytestapplication.camera.OldCameraDetection;

/**
 * Created by USER on 28/03/2017.
 */

public class CameraUtils {
    private static final String TAG = "CameraUtils";

    protected boolean mCameraIsOpened = false;
    private ICamera iCamera;

    public CameraUtils(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iCamera = new CameraDetection(context);
        } else {
            iCamera = new OldCameraDetection();
        }
    }

    /**
     * Check whether camera is in used by other application.
     *
     * @return true if it occupied.
     * @throws ClassCastException in case android.hardware.Camera not found.
     */
    public static boolean isCameraRunning() throws Exception {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) camera.release();
        }
        return false;
    }

    public boolean isOpen() {
        boolean cameraOpen = iCamera.isOpen(); // cameraIsOpened();
        if (true == cameraOpen && false == mCameraIsOpened) {
            mCameraIsOpened = true;
            Log.d(TAG, "Camera is Opened");
        } else if (false == cameraOpen && true == mCameraIsOpened) {
            mCameraIsOpened = false;
            Log.d(TAG, "Camera is not Opened");
        }
        return mCameraIsOpened;
    }
}
