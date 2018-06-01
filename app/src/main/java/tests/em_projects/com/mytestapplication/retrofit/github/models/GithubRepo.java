package tests.em_projects.com.mytestapplication.retrofit.github.models;

public class GithubRepo {

    public String name;
    public String owner;
    public String url;

    @Override
    public String toString() {
        return (name + " " + url);
    }
}
