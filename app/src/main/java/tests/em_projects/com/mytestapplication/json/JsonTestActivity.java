package tests.em_projects.com.mytestapplication.json;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by eyalmuchtar on 3/11/18.
 */

public class JsonTestActivity extends Activity {
    private static final String TAG = "JsonTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String input = "{\\\"name\\\":\\\"orion\\\",\\\"uuid\\\":\\\"b38460f2-1721-426b-a489-430010b8b93a\\\"}";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ('\\' == c) continue;
            sb.append(c);
        }
        input = sb.toString();
        Log.d(TAG, "input: " + input);
    }
}
