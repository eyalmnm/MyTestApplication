package tests.em_projects.com.mytestapplication.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tests.em_projects.com.mytestapplication.retrofit.StackOverFlow.view.RetrofitStackoverflowMainActivity;
import tests.em_projects.com.mytestapplication.retrofit.gerrit.Controller;

public class RetrofitDemoActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitDemoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Java Based Retrofit Sample
        Controller controller = new Controller();
        controller.start();

        // StackOverFlow Using RecyclerView and Retrofit
        Intent intent = new Intent(RetrofitDemoActivity.this, RetrofitStackoverflowMainActivity.class);
        startActivity(intent);
    }
}
