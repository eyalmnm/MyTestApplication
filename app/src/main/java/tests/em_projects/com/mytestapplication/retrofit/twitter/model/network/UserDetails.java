package tests.em_projects.com.mytestapplication.retrofit.twitter.model.network;

import com.google.gson.annotations.SerializedName;

public class UserDetails {

    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
