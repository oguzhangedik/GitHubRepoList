package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RestInterfaceController;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RetrofitClient;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.InternetConnectionUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);


        if (!InternetConnectionUtil.isConnected()) {
            Toast.makeText(this, "Check your network connn", Toast.LENGTH_LONG).show();
            return;
        }

        RestInterfaceController service = RetrofitClient.getClient(getApplicationContext()).create(RestInterfaceController.class);

        Call<List<Repo>> repos = service.fetchRepos("oguzhangedik");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                Toast.makeText(RepoListActivity.this, "doldu hocam doldu! " + response.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(RepoListActivity.this, "Kahretsin! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
