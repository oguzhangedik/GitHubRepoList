package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.AppConstants;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.FavoriteReposUtil;

public class RepoDetailActivity extends AppCompatActivity {

    private Repo currentRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        currentRepo = (Repo) getIntent().getSerializableExtra(AppConstants.SELECTED_REPO);
        if (currentRepo != null) {
            setTitle(currentRepo.getName());
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_access_to_selected_repo_warning), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repo_detail, menu);
        menu.getItem(0).setIcon(currentRepo.getIsFavorite() ? R.mipmap.ticked_star_icon : R.mipmap.plus_star_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            currentRepo.setIsFavorite(!currentRepo.getIsFavorite());
            if (currentRepo.getIsFavorite()) {
                item.setIcon(R.mipmap.ticked_star_icon);
                FavoriteReposUtil.addOrUpdate(this, currentRepo);
            } else {
                item.setIcon(R.mipmap.plus_star_icon);
                FavoriteReposUtil.remove(this, currentRepo);
            }

            Toast.makeText(this, currentRepo.getIsFavorite()
                    ? getString(R.string.added_to_favorite_list)
                    : getString(R.string.remove_from_favorites), Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();

        returnIntent.putExtra(AppConstants.SELECTED_REPO, (Parcelable) currentRepo);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }
}
