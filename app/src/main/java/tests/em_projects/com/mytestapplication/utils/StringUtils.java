package tests.em_projects.com.mytestapplication.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by eyal on 20/04/16.
 */
public class StringUtils {

    /**
     * Chaeck whether a given string is valid
     *
     * @param str the string to be checked
     * @return boolean indicate string invalidity.
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;
        str = str.trim().replace("?", "").replace("<", "").replace(">", "").replace("&", "").replace("\"", "").replace("\'", "").replace(";", "");
        if (str.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * Check the a string contains the other ignoring the case
     *
     * @param theString the big string
     * @param str       the other one
     * @return true if theString contains str
     */
    public static boolean containsDifferentCases(String theString, String str) {
        String firstString = theString.toUpperCase();
        String secondString = str.toUpperCase();
        return firstString.contains(secondString);
    }

    /**
     * Format time as milis to human readable time
     *
     * @param millis the time to convert
     * @return the time as String
     */
    public static String formatDateToString(long millis) {
        Log.d("marga", millis + "");
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    /**
     * Format time as milis to human readable time
     *
     * @param time the time to convert
     * @return the time as String
     */
    public static String formatTimeToString(int time) {
        int hours = time/60;
        int minutes = time%60;

        return ""+(hours<10?"0"+hours:hours)+":"+(minutes<10?"0"+minutes:minutes);
    }
}