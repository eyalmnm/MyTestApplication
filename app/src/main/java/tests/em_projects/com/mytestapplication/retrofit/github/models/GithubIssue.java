package tests.em_projects.com.mytestapplication.retrofit.github.models;

import com.google.gson.annotations.SerializedName;

public class GithubIssue {

    public String id;
    public String title;
    public String comments_url;

    @SerializedName("body")
    public String comment;

    @Override
    public String toString() {
        return id + " - " + title;
    }
}
