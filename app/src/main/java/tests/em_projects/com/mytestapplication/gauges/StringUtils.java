package tests.em_projects.com.mytestapplication.gauges;

import android.util.Log;

import java.util.concurrent.TimeUnit;


public class StringUtils {

    public static String formatTimeToString(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        return "" + (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
    }

    public static String formatDateToString(long millis) {
        Log.d("marga", millis + "");
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    public static String formatDuration(long millis) {
        return String.format("%02dh %02dmin",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }
}
