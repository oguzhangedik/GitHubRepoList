package com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs;

import android.annotation.SuppressLint;
import android.os.Parcel;

@SuppressLint("ParcelCreator")
public class NoRepo extends RepoItemBase {

    private String message;

    public NoRepo(String message){
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
