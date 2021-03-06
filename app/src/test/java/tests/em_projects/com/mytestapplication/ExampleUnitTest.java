package tests.em_projects.com.mytestapplication;

import org.junit.Test;

import tests.em_projects.com.mytestapplication.utils.CameraUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkCameraUtils() {
        boolean isRunning = false;
        try {
            isRunning = CameraUtils.isCameraRunning();
        } catch (Throwable e) {
            System.out.println(TAG + ": " + e.getMessage());
        }
        assertFalse("check Camera Utils", isRunning);
    }
}