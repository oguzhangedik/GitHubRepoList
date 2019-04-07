package com.ingbank.mobileapp.githubrepolist.githubrepolist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.FavoriteRepos;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteReposUtil {

    private static final String FAVORITE_REPOS_SP_KEY = "FAVORITE_REPOS_SP_KEY";
    private static final String FAVORITE_REPO_LIST_SP_OBJECT_KEY = "FAVORITE_REPO_LIST_SP_OBJECT_KEY";

    public static void saveFavorites(Context context, FavoriteRepos favoriteFavoriteRepos) {
        SharedPreferences mPrefs = context.getSharedPreferences(FAVORITE_REPOS_SP_KEY, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteFavoriteRepos);
        prefsEditor.putString(FAVORITE_REPO_LIST_SP_OBJECT_KEY, json);
        prefsEditor.apply();
    }

    public static FavoriteRepos getFavorites(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences(FAVORITE_REPOS_SP_KEY, MODE_PRIVATE);
        String json = mPrefs.getString(FAVORITE_REPO_LIST_SP_OBJECT_KEY, "");

        if (TextUtils.isEmpty(json)) {
            saveFavorites(context, new FavoriteRepos());
            return getFavorites(context);
        }

        return new Gson().fromJson(json, FavoriteRepos.class);
    }

    public static List<Repo> mapFavorites(Context context, List<Repo> incomingRepoList) {

        List<Repo> favoriteRepoList = getFavorites(context).getRepos();

        for (Repo incomingRepo : incomingRepoList) {
            for (Repo favoriteRepo : favoriteRepoList) {
                if (incomingRepo.getId() == favoriteRepo.getId()) {
                    incomingRepo.setIsFavorite(true);
                    break;
                }
            }
        }
        return incomingRepoList;
    }

    public static void addOrUpdate(Context context, Repo selectedRepo) {
        Repo existFavoriteRepo = null;
        FavoriteRepos favoriteFavoriteRepos = getFavorites(context);
        List<Repo> favoriteRepoList = favoriteFavoriteRepos.getRepos();

        for (Repo repo : favoriteRepoList) {
            if (repo.getId() == selectedRepo.getId()) {
                existFavoriteRepo = repo;
                break;
            }

        }

        if (null != existFavoriteRepo) {
            favoriteRepoList.remove(existFavoriteRepo);
        }

        favoriteRepoList.add(selectedRepo);

        saveFavorites(context, favoriteFavoriteRepos);
    }

    public static void remove(Context context, Repo selectedRepo) {
        FavoriteRepos favoriteFavoriteRepos = getFavorites(context);
        List<Repo> favoriteRepoList = favoriteFavoriteRepos.getRepos();

        Repo repoToRemove = null;

        for (Repo repo : favoriteRepoList) {
            if (repo.getId() == selectedRepo.getId()) {
                repoToRemove = repo;
                break;
            }
        }

        if (null != repoToRemove) {
            favoriteRepoList.remove(repoToRemove);
            saveFavorites(context, favoriteFavoriteRepos);
        }
    }
}
