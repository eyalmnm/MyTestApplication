package tests.em_projects.com.mytestapplication.camera;

import android.hardware.Camera;

/**
 * Created by USER on 08/05/2017.
 */

public class OldCameraDetection implements ICamera {

    @Override
    public boolean isOpen() {
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
}
