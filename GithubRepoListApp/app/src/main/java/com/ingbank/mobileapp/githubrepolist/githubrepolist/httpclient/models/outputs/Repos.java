package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Repos {
    @SerializedName("data")
    @Expose
    private List<Repo> repos = null;

    public List<Repo> getRepos() {
        return repos;
    }
}
