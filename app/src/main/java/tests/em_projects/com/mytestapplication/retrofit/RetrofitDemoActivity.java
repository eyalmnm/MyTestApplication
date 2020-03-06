package tests.em_projects.com.mytestapplication.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tests.em_projects.com.mytestapplication.retrofit.StackOverFlow.view.RetrofitStackoverflowMainActivity;
import tests.em_projects.com.mytestapplication.retrofit.gerrit.Controller;
import tests.em_projects.com.mytestapplication.retrofit.github.views.activities.RetroMainActivity;
import tests.em_projects.com.mytestapplication.retrofit.twitter.views.activities.RetrofitTwitterMainActivity;

public class RetrofitDemoActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitDemoActivity";

    private final boolean USE_GERRIT = false;
    private final boolean USE_GIT_HUB = false;
    private final boolean USE_TWITTER = true;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // Java Based Retrofit Sample
        Controller controller = new Controller();
        controller.start();

        // StackOverFlow Using RecyclerView and Retrofit
        if (true == USE_GIT_HUB) {
            Intent intent = new Intent(context, RetroMainActivity.class);
            startActivity(intent);
        } else if (true == USE_TWITTER) {
            Intent intent = new Intent(context, RetrofitTwitterMainActivity.class);
            startActivity(intent);
        } else if (true == USE_GERRIT) {
            // GitHub Using RxJava, RecyclerView and Retrofit
            Intent intent = new Intent(context, RetrofitStackoverflowMainActivity.class);
            startActivity(intent);
        }
    }
}
