package com.ingbank.mobileapp.githubrepolist.githubrepolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoItemBase;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RepoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HHEADER = 1;

    private List<RepoItemBase> repoList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RepoListAdapter(Context context, List<RepoItemBase> repoList) {
        this.mInflater = LayoutInflater.from(context);
        this.repoList = repoList;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HHEADER) {
            View view = mInflater.inflate(R.layout.recyclerview_header_item, parent, false);
            return new HeaderItemViewHolder(view);

        } else {
            View view = mInflater.inflate(R.layout.recyclerview_repo_item, parent, false);
            return new RepoItemViewHolder(view);
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderItemViewHolder) {
            HeaderItemViewHolder headerItemViewHolder = (HeaderItemViewHolder) holder;
            headerItemViewHolder.myTextView.setText("HEADDERRR");
        } else if(holder instanceof RepoItemViewHolder) {
            Repo repo = (Repo) repoList.get(position);
            RepoItemViewHolder repoItemViewHolder = (RepoItemViewHolder) holder;
            repoItemViewHolder.myTextView.setText(repo.getName());
        }

    }

    @Override
    public int getItemCount() {
        return repoList ==  null ? 0 : repoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0 == position
            ? VIEW_TYPE_HHEADER
            : super.getItemViewType(position);
    }


    public class HeaderItemViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        HeaderItemViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
        }
    }

    public class RepoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        RepoItemViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public RepoItemBase getItem(int id) {
        return repoList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
