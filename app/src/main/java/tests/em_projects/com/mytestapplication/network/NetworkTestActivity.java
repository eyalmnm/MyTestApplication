package tests.em_projects.com.mytestapplication.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.JSONUtils;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

public class NetworkTestActivity extends AppCompatActivity {
    private static final String TAG = "NetworkTestActivity";

    private TextView textView;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
        textView = findViewById(R.id.textView);

        handler = new Handler(getMainLooper()) {
            /**
             * Subclasses must implement this to receive messages.
             *
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                textView.setText((String) msg.obj);
            }
        };

        ServerUtilities.getInstance().getRegions(new CommListener() {
            @Override
            public void newDataArrived(String response) {
                Log.d(TAG, "newDataArrived: Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String jsonStr = jsonObject.toString();
                    Log.d(TAG, "newDataArrived: Json: " + jsonStr);
                    Log.d(TAG, "newDataArrived: response heb: " + StringUtils.convertToUTF8(response));
                    JSONObject dataObject = JSONUtils.getJSONObjectValue(jsonObject, "data");
                    JSONArray regionsArray = JSONUtils.getJsonArray(dataObject, "regions");
                    JSONObject regionObject = regionsArray.getJSONObject(0);
                    String title = JSONUtils.getStringValue(regionObject, "itemTitle");

                    handler.sendMessage(handler.obtainMessage(123, title));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void exceptionThrown(Throwable throwable) {
                Log.d(TAG, "Error: " + throwable.getMessage());
            }
        });
    }
}
