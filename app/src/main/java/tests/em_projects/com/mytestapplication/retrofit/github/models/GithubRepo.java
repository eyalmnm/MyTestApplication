package tests.em_projects.com.mytestapplication.retrofit.github.models;

public class GithubRepo {

    String name;
    String owner;
    String url;

    @Override
    public String toString() {
        return (name + " " + url);
    }
}
