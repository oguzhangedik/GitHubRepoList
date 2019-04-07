package com.ingbank.mobileapp.githubrepolist.githubrepolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;


import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.AppConstants;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.utils.FavoriteReposUtil;
import com.squareup.picasso.Picasso;

public class RepoDetailActivity extends AppCompatActivity {

    @BindView(R.id.ownerImageView)
    ImageView ownerImageView;

    @BindView(R.id.ownerNameTextView)
    TextView ownerNameTextView;

    @BindView(R.id.starCountTextView)
    TextView starCountTextView;

    @BindView(R.id.openIssuesTextView)
    TextView openIssuesTextView;


    private Repo currentRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        currentRepo = (Repo) getIntent().getSerializableExtra(AppConstants.SELECTED_REPO);
        if (currentRepo != null) {
            setTitle(currentRepo.getName());

            Picasso.with(this)
                    .load(currentRepo.getOwner().getAvatarUrl())
                    .error(R.drawable.profile_image_error_icon)
                    .placeholder(R.drawable.progress_icon)
                    .into(ownerImageView);

            ownerNameTextView.setText(currentRepo.getOwner().getLogin());
            starCountTextView.setText(getString(R.string.star_count_label) + " " + currentRepo.getStargazersCount());
            openIssuesTextView.setText(getString(R.string.open_issues_label) + " " + currentRepo.getOpenIssuesCount());

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
