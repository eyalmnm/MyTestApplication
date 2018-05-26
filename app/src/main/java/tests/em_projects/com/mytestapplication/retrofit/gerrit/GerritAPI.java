package tests.em_projects.com.mytestapplication.retrofit.gerrit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GerritAPI {

    @GET("changes/")
    Call<List<Change>> loadChanges(@Query("q") String status);
}
