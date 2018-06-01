package tests.em_projects.com.mytestapplication.retrofit.github.views.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.retrofit.github.models.GithubIssue;
import tests.em_projects.com.mytestapplication.retrofit.github.models.GithubRepo;
import tests.em_projects.com.mytestapplication.retrofit.github.models.helpers.GithubRepoDeserializer;
import tests.em_projects.com.mytestapplication.retrofit.github.models.network.interfaces.GithubAPI;
import tests.em_projects.com.mytestapplication.retrofit.github.views.dialogs.CredentialsDialog;

// Ref: https://stackoverflow.com/questions/29916962/javax-net-ssl-sslhandshakeexception-javax-net-ssl-sslprotocolexception-ssl-han
// Ref: https://stackoverflow.com/a/36892715/341497
public class RetroMainActivity extends AppCompatActivity implements CredentialsDialog.ICredentialsDialogListener {
    private static final String TAG = "RetroMainActivity";

    private GithubAPI githubAPI;
    private String username;
    private String password;

    private Spinner repositoriesSpinner;
    private Spinner issuesSpinner;
    private EditText commentEditText;
    private Button sendButton;
    private Button loadReposButtons;

    // RxJava
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retro_main);
        Log.d(TAG, "onCreate");

        context = this;
        updateAndroidSecurityProvider(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        sendButton = (Button) findViewById(R.id.send_comment_button);

        repositoriesSpinner = (Spinner) findViewById(R.id.repositories_spinner);
        repositoriesSpinner.setEnabled(false);
        repositoriesSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"No repositories available"}));
        repositoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected -> GithubRepo: " + parent.getSelectedItem().toString());
                if (parent.getSelectedItem() instanceof GithubRepo) {
                    GithubRepo githubRepo = (GithubRepo) parent.getSelectedItem();
                    compositeDisposable.add(githubAPI.getIssues(githubRepo.owner, githubRepo.name)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getIssuesObserver()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        issuesSpinner = (Spinner) findViewById(R.id.issues_spinner);
        issuesSpinner.setEnabled(false);
        issuesSpinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Please select repository"}));

        commentEditText = (EditText) findViewById(R.id.comment_edittext);

        loadReposButtons = (Button) findViewById(R.id.loadRepos_button);

        createGithubAPI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_retofit_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.menu_credentials:
                showCredentialsDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCredentialsDialog() {
        Log.d(TAG, "showCredentialsDialog");
        CredentialsDialog dialog = new CredentialsDialog();
        Bundle arguments = new Bundle();
        arguments.putString("username", username);
        arguments.putString("password", password);
        dialog.setArguments(arguments);

        dialog.show(getFragmentManager(), "credentialsDialog");
    }

    private void createGithubAPI() {
        Log.d(TAG, "createGithubAPI");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(GithubRepo.class, new GithubRepoDeserializer())
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                                Credentials.basic(username, password));

                        Request newRequest = builder.build();
                        Response response = chain.proceed(newRequest);
                        return response;
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubAPI.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        githubAPI = retrofit.create(GithubAPI.class);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");
        switch (view.getId()) {
            case R.id.loadRepos_button:
                compositeDisposable.add(githubAPI.getRepos()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getRepositoriesObserver()));
                break;
            case R.id.send_comment_button:
                String newComment = commentEditText.getText().toString();
                if (!newComment.isEmpty()) {
                    GithubIssue selectedItem = (GithubIssue) issuesSpinner.getSelectedItem();
                    selectedItem.comment = newComment;
                    compositeDisposable.add(githubAPI.postComment(selectedItem.comments_url, selectedItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(getCommentObserver()));
                } else {
                    Toast.makeText(context, "Please enter a comment", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private DisposableSingleObserver<List<GithubRepo>> getRepositoriesObserver() {
        return new DisposableSingleObserver<List<GithubRepo>>() {
            @Override
            public void onSuccess(List<GithubRepo> value) {
                Log.d(TAG, "getRepositoriesObserver -> onSuccess");
                if (!value.isEmpty()) {
                    ArrayAdapter<GithubRepo> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, value);
                    repositoriesSpinner.setAdapter(spinnerAdapter);
                    repositoriesSpinner.setEnabled(true);
                } else {
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"User has no repositories"});
                    repositoriesSpinner.setAdapter(spinnerAdapter);
                    repositoriesSpinner.setEnabled(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getRepositoriesObserver -> onError", e);
                Toast.makeText(context, "Can not load repositories", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private DisposableSingleObserver<List<GithubIssue>> getIssuesObserver() {
        return new DisposableSingleObserver<List<GithubIssue>>() {
            @Override
            public void onSuccess(List<GithubIssue> value) {
                Log.d(TAG, "getIssuesObserver -> onSuccess");
                if (!value.isEmpty()) {
                    ArrayAdapter<GithubIssue> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, value);
                    issuesSpinner.setEnabled(true);
                    commentEditText.setEnabled(true);
                    sendButton.setEnabled(true);
                    issuesSpinner.setAdapter(spinnerAdapter);
                } else {
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, new String[]{"Repository has no issues"});
                    issuesSpinner.setEnabled(false);
                    commentEditText.setEnabled(false);
                    sendButton.setEnabled(false);
                    issuesSpinner.setAdapter(spinnerAdapter);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getIssuesObserver -> onError", e);
                Toast.makeText(context, "Can not load issues", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private DisposableSingleObserver<ResponseBody> getCommentObserver() {
        return new DisposableSingleObserver<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody value) {
                Log.d(TAG, "getCommentObserver -> onSuccess");
                commentEditText.setText("");
                Toast.makeText(context, "Comment created", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getCommentObserver -> onError", e);
                Toast.makeText(context, "Can not create comment", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDialogPositiveClick(String username, String password) {
        Log.d(TAG, "onDialogPositiveClick");
        this.username = username;
        this.password = password;
        loadReposButtons.setEnabled(true);
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(context);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "SecurityException -> Google Play Services not available.");
        }
    }
}
