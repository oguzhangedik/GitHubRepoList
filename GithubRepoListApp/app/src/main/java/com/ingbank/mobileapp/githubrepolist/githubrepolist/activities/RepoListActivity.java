package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.adapters.RepoListAdapter;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RestInterfaceController;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RetroClient;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoHeader;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoItemBase;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.InternetConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListActivity extends AppCompatActivity implements RepoListAdapter.ItemClickListener{

    private RepoListAdapter repoListAdapter;
    private RecyclerView recyclerViewForRepo;
    private List<RepoItemBase> repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);

        if (!InternetConnectionUtil.isConnected()) {
            Toast.makeText(this, "Check your network connection", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerViewForRepo = findViewById(R.id.recyclerViewForRepo);
        recyclerViewForRepo.setLayoutManager(new LinearLayoutManager(this));

        RestInterfaceController service = RetroClient.getClient(getApplicationContext()).create(RestInterfaceController.class);

        Call<List<Repo>> repos = service.fetchRepos("oguzhangedik");
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                Toast.makeText(RepoListActivity.this, "Good! " + response.toString(), Toast.LENGTH_SHORT).show();

                repoList = new ArrayList<>();
                repoList.add(new RepoHeader());

                if (response.body() != null) {
                    repoList.addAll(response.body());
                }

                repoListAdapter = new RepoListAdapter(RepoListActivity.this, repoList);
                repoListAdapter.setClickListener(RepoListActivity.this);
                recyclerViewForRepo.setAdapter(repoListAdapter);
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Toast.makeText(RepoListActivity.this, "Error! " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + ((Repo) repoList.get(position)).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
