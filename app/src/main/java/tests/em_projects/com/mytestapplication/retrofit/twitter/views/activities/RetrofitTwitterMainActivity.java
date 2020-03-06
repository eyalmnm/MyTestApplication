package tests.em_projects.com.mytestapplication.retrofit.twitter.views.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.OAuthToken;
import tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.UserDetails;
import tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.api.TwitterApi;

public class RetrofitTwitterMainActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitTwitterMainAct";

    private String credentials = Credentials.basic("aConsumerKey", "aSecret");

    private Button requestTokenButton;
    private Button requestUserDetailsButton;
    private EditText usernameEditText;
    private TextView usernameTextView;

    private TextView nameTextView;
    private TextView locationTextView;
    private TextView urlTextView;
    private TextView descriptionTextView;

    private TwitterApi twitterApi;
    private OAuthToken token;

    private Context context;
    Callback<OAuthToken> tokenCallback = new Callback<OAuthToken>() {
        @Override
        public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
            Log.d(TAG, "tokenCallback -> onResponse");
            if (response.isSuccessful()) {
                requestTokenButton.setEnabled(false);
                requestUserDetailsButton.setEnabled(true);
                usernameTextView.setEnabled(true);
                usernameEditText.setEnabled(true);
                token = response.body();
            } else {
                Toast.makeText(context, "Failure while requesting token", Toast.LENGTH_LONG).show();
                Log.d("RequestTokenCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<OAuthToken> call, Throwable t) {
            Log.e(TAG, "tokenCallback -> onFailure: ", t);
        }
    };
    Callback<UserDetails> userDetailsCallback = new Callback<UserDetails>() {
        @Override
        public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
            Log.d(TAG, "userDetailsCallback -> onResponse");
            if (response.isSuccessful()) {
                UserDetails userDetails = response.body();
                nameTextView.setText(userDetails.getName() == null ? "no value" : userDetails.getName());
                locationTextView.setText(userDetails.getLocation() == null ? "no value" : userDetails.getLocation());
                urlTextView.setText(userDetails.getUrl() == null ? "no value" : userDetails.getUrl());
                descriptionTextView.setText(userDetails.getDescription().isEmpty() ? "no value" : userDetails.getDescription());
            } else {
                Toast.makeText(context, "Failure while requesting user details", Toast.LENGTH_LONG).show();
                Log.d("UserDetailsCallback", "Code: " + response.code() + "Message: " + response.message());
            }
        }

        @Override
        public void onFailure(Call<UserDetails> call, Throwable t) {
            Log.e(TAG, "userDetailsCallback -> onFailure: ", t);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retro_twitter_main);
        Log.d(TAG, "onCreate");

        context = this;
        updateAndroidSecurityProvider(this);

        requestTokenButton = findViewById(R.id.request_token_button);
        requestUserDetailsButton = findViewById(R.id.request_user_details_button);
        usernameEditText = findViewById(R.id.username_edittext);
        usernameTextView = findViewById(R.id.username_textview);

        nameTextView = findViewById(R.id.name_textview);
        locationTextView = findViewById(R.id.location_textview);
        urlTextView = findViewById(R.id.url_textview);
        descriptionTextView = findViewById(R.id.description_textview);

        createTwitterApi();
    }

    private void createTwitterApi() {
        Log.d(TAG, "createTwitterApi");
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        token != null ? token.getAuthorization() : credentials);

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TwitterApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        twitterApi = retrofit.create(TwitterApi.class);
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");
        switch (view.getId()) {
            case R.id.request_token_button:
                twitterApi.postCredentials("client_credentials").enqueue(tokenCallback);
                break;
            case R.id.request_user_details_button:
                String editTextInput = usernameEditText.getText().toString();
                if (!editTextInput.isEmpty()) {
                    twitterApi.getUserDetails(editTextInput).enqueue(userDetailsCallback);
                } else {
                    Toast.makeText(this, "Please provide a username", Toast.LENGTH_LONG).show();
                }
                break;
        }
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
