package tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.OAuthToken;
import tests.em_projects.com.mytestapplication.retrofit.twitter.model.network.UserDetails;

public interface TwitterApi {

    String BASE_URL = "https://api.twitter.com/";

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<OAuthToken> postCredentials(@Field("grant_type") String grantType);

    @GET("/1.1/users/show.json")
    Call<UserDetails> getUserDetails(@Query("screen_name") String name);
}
