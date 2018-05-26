package tests.em_projects.com.mytestapplication.retrofit.StackOverFlow.models.retrofit_connection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tests.em_projects.com.mytestapplication.retrofit.StackOverFlow.models.Answer;
import tests.em_projects.com.mytestapplication.retrofit.StackOverFlow.models.Question;

public interface StackOverflowAPI {
    String BASE_URL = "https://api.stackexchange.com";

    @GET("/2.2/questions?order=desc&sort=votes&site=stackoverflow&tagged=android&filter=withbody")
    Call<List<Question>> getQuestions();

    @GET("/2.2/questions/{id}/answers?order=desc&sort=votes&site=stackoverflow")
    Call<List<Answer>> getAnswersForQuestion(@Path("id") String questionId);
}
