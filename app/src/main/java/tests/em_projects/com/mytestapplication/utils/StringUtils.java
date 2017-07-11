package tests.em_projects.com.mytestapplication.utils;

import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    /**
     * Check whether the given string array contains the given string
     *
     * @param strArr the String array
     * @param str    the String
     * @return true if it contained
     */
    public static boolean isContained(String[] strArr, String str) {
        if (true == isNullOrEmpty(str)) return false;
        if (null == strArr || 0 == strArr.length) return false;
        for (String string : strArr) {
            if (str.equals(string)) return true;
        }
        return false;
    }

    /**
     * Check phone number validation
     *
     * @param phoneNumber the given phone number
     * @return is valid
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phoneNumber).matches();
    }

    /**
     * Create a string for the given ArrayList using the given delimiter as separator
     *
     * @param strings   the strings to create a String
     * @param delimiter the separator between the strings
     * @return a String that comtains all the given Strings separated by the given delimiterß
     */
    public static String stringToString(ArrayList<String> strings, String delimiter) {
        String delim = "";
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            sb.append(delim);
            delim = delimiter;
            sb.append(str);
        }
        return sb.toString();
    }
}