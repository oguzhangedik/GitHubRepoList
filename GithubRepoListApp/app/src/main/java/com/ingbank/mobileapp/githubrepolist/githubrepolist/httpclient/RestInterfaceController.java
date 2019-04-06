package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestInterfaceController {

    @GET("users/{user}/repos")
    Call<List<Repo>> fetchRepos(@Path("user") String user);

}

