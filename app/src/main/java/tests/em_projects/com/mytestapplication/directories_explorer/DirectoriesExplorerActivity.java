package tests.em_projects.com.mytestapplication.directories_explorer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 09/10/2017.
 */

// requires "android.permission.READ_EXTERNAL_STORAGE" permissions

public class DirectoriesExplorerActivity extends Activity {
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().toString();
    private static final String TAG = "DirectoriesExplorerAct";
    private TextView outputTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories_explorer);

        outputTextView = (TextView) findViewById(R.id.outputTextView);

        File baseDir = new File(BASE_PATH);
        findDirctories(baseDir, BASE_PATH);
    }

    private void findDirctories(File dir, String name) {
        if (true == dir.isDirectory()) {
            outputTextView.append(name + "\n");
            Log.d(TAG, "dir name: " + name);
            File[] files = dir.listFiles();
            if (null == files || 0 == files.length) return;
            for (File file : files) {
                findDirctories(file, name + "/" + file.getName());
            }
        }
    }
}
