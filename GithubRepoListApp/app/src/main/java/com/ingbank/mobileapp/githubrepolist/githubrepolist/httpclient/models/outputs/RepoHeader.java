package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs;

import android.annotation.SuppressLint;
import android.os.Parcel;

@SuppressLint("ParcelCreator")
public class RepoHeader extends RepoItemBase {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
