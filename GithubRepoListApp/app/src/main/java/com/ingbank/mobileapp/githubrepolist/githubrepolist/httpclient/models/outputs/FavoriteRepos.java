package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepos {
    private List<Repo> repos;

    public FavoriteRepos() {
        this.repos = new ArrayList<>();
    }


    public List<Repo> getRepos() {
        return repos;
    }

    public FavoriteRepos setRepos(List<Repo> repos) {
        this.repos = repos;
        return this;
    }
}
