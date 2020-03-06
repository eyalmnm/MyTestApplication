package tests.em_projects.com.mytestapplication.frames_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tests.em_projects.com.mytestapplication.R;

public class TwoFramesTestActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_frames_test);

        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TwoFramesTestActivity.this, "Button pressed", Toast.LENGTH_SHORT).show();
            }
        });

        TextView testButtonTwo = findViewById(R.id.testButtonTwo);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TwoFramesTestActivity.this, "Button two pressed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
