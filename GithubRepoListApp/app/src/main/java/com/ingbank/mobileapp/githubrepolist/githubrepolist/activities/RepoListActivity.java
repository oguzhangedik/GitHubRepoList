package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.adapters.RepoListAdapter;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RestInterfaceController;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.RetroClient;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.NoRepo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoHeader;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoItemBase;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.AppConstants;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.FavoriteReposUtil;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.InternetConnectionUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.HttpConstants.INTERNAL_SERVER_ERROR;
import static com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.HttpConstants.NOT_FOUND;
import static com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.AppConstants.SELECTED_REPO_REQUEST_CODE;

public class RepoListActivity extends AppCompatActivity implements RepoListAdapter.ItemClickListener, RepoListAdapter.RepoSearchingInstructionListener {

    @BindView(R.id.repoLoadingProgressBar)
    ProgressBar repoLoadingProgressBar;

    @BindView(R.id.recyclerViewForRepo)
    RecyclerView recyclerViewForRepo;

    private RepoListAdapter repoListAdapter;
    private List<RepoItemBase> repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        ButterKnife.bind(this);
        setTitle(R.string.home_page);
        recyclerViewForRepo.setLayoutManager(new LinearLayoutManager(this));

        repoList = new ArrayList<>();
        repoList.add(new RepoHeader());

        repoListAdapter = new RepoListAdapter(this, repoList, this);
        repoListAdapter.setRepoItemClickListener(this);
        recyclerViewForRepo.setAdapter(repoListAdapter);

        fetchRepoList(getResources().getString(R.string.my_github_username));
    }

    private void fetchRepoList(String username) {
        if (!InternetConnectionUtil.isConnected()) {
            Toast.makeText(this, getString(R.string.network_connection_error), Toast.LENGTH_LONG).show();
            repoLoadingProgressBar.setVisibility(View.GONE);
            return;
        }

        RestInterfaceController service = RetroClient.getClient(getApplicationContext()).create(RestInterfaceController.class);
        Call<List<Repo>> repos = service.fetchRepos(username);
        repoLoadingProgressBar.setVisibility(View.VISIBLE);
        repos.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                repoLoadingProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    updateRepoList(response.body());
                } else {
                    String errorMessage;
                    switch (response.code()) {
                        case NOT_FOUND:
                            errorMessage = getString(R.string.repo_not_found);
                            break;
                        case INTERNAL_SERVER_ERROR:
                            errorMessage = getString(R.string.server_broken);
                            break;
                        default:
                            errorMessage = getResources().getString(R.string.unknown_error);
                            break;
                    }
                    Toast.makeText(RepoListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    NoRepo noRepoItem = new NoRepo(errorMessage);
                    clearRepoListDataSet();
                    repoList.add(noRepoItem);
                    repoListAdapter.notifyDataSetChanged();
                }

            }

            private void updateRepoList(List<Repo> repoListToUpdate) {
                clearRepoListDataSet();
                repoList.addAll(FavoriteReposUtil.mapFavorites(RepoListActivity.this, repoListToUpdate));
                repoListAdapter.notifyDataSetChanged();
            }

            private void clearRepoListDataSet() {
                for (int index = repoList.size() - 1; index >= 0; --index) {
                    RepoItemBase currentRepo = repoList.get(index);
                    if (!(currentRepo instanceof RepoHeader)) {
                        repoList.remove(currentRepo);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                repoLoadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(RepoListActivity.this, "Error! " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        RepoItemBase selectedBaseRepo = repoList.get(position);

        if (!(selectedBaseRepo instanceof Repo)) {
            return;
        }
        Repo selectedRepo = (Repo) selectedBaseRepo;

        Intent myIntent = new Intent(this, RepoDetailActivity.class);
        myIntent.putExtra(AppConstants.SELECTED_REPO, (Parcelable) selectedRepo);
        RepoListActivity.this.startActivityForResult(myIntent, SELECTED_REPO_REQUEST_CODE);
    }

    @Override
    public void onSubmitButtonClicked(String searchText) {
        fetchRepoList(searchText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (AppConstants.SELECTED_REPO_REQUEST_CODE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Repo incomingRepo = data.getParcelableExtra(AppConstants.SELECTED_REPO);

                for (int index = 0; index < repoList.size(); ++index) {
                    RepoItemBase baseRepo = repoList.get(index);
                    if (baseRepo instanceof Repo) {
                        Repo repo = (Repo) baseRepo;
                        if (incomingRepo.getId() == repo.getId()) {
                            repo.setIsFavorite(incomingRepo.getIsFavorite());
                            repoListAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
    }
}

