package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.adapters.MyRecyclerViewAdapter;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RestInterfaceController;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RetrofitClient;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.InternetConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private MyRecyclerViewAdapter adapter;
    private ArrayList<String> animalNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);


        // data to populate the RecyclerView with
        animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + animalNames.get(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
