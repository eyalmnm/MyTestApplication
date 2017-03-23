package tests.em_projects.com.mytestapplication.data_usage;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by eyal on 27/04/16.
 */

// Ref: http://stackoverflow.com/questions/17674790/how-do-i-programmatically-show-data-usage-of-all-applications
// Ref: http://stackoverflow.com/questions/7554799/best-way-to-evaluate-connection-speed
// Ref: http://stackoverflow.com/questions/32153318/httpclient-wont-import-in-android-studio

public class DataUsageActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
//
//        for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
//
//            // Get UID of the selected process
////            int uid = ((ActivityManager.RunningAppProcessInfo)getListAdapter().getItem(position)).uid;
//            int uid = runningApp.uid;
//            String name = runningApp.processName;
//
//            // Get traffic data
//            long received = TrafficStats.getUidRxBytes(uid);
//            long send = TrafficStats.getUidTxBytes(uid);
//            Log.v("" + uid, "Send :" + send + ", Received :" + received);
//        }

        new NetworkQualityTester().execute("www.google.com");
    }


    private class NetworkQualityTester extends AsyncTask<String, Void, String> {

        /*
                In app's build.gradle add:

                android {
                    useLibrary 'org.apache.http.legacy'
                }
         */
        private long startTime;
        private long endTime;
        private long takenTime;

        protected String doInBackground(String... urls) {
            String response = "";

            startTime = System.currentTimeMillis();
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {

                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();


                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                    endTime = System.currentTimeMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            long dataSize = result.length() / 1024;
            takenTime = endTime - startTime;
            long s = takenTime / 1000;
            double speed = dataSize / s;

            Toast.makeText(context, "" + speed + "kbps", Toast.LENGTH_SHORT).show();
        }
    }
}
