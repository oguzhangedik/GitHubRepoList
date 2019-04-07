package com.ingbank.mobileapp.githubrepolist.githubrepolist.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingbank.mobileapp.githubrepolist.githubrepolist.R;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.NoRepo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.Repo;
import com.ingbank.mobileapp.githubrepolist.githubrepolist.httpclient.models.outputs.RepoItemBase;

import java.util.List;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

public class RepoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_NO_REPO = 2;

    private List<RepoItemBase> repoList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private RepoSearchingInstructionListener repoSearchingInstructionListener;

    // data is passed into the constructor
    public RepoListAdapter(Context context, List<RepoItemBase> repoList, RepoSearchingInstructionListener repoSearchingInstructionListener) {
        this.mInflater = LayoutInflater.from(context);
        this.repoList = repoList;
        this.repoSearchingInstructionListener = repoSearchingInstructionListener;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = mInflater.inflate(R.layout.recyclerview_header_item, parent, false);
            return new HeaderItemViewHolder(view);

        } else if (viewType == VIEW_TYPE_NO_REPO) {
            View view = mInflater.inflate(R.layout.recyclerview_no_repo_item, parent, false);
            return new NoRepoViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.recyclerview_repo_item, parent, false);
            return new RepoItemViewHolder(view);
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderItemViewHolder) {
            HeaderItemViewHolder headerItemViewHolder = (HeaderItemViewHolder) holder;
        } else if (holder instanceof NoRepoViewHolder) {
            NoRepo noRepoItem = (NoRepo) repoList.get(position);
            NoRepoViewHolder noRepoViewHolder = (NoRepoViewHolder) holder;
            noRepoViewHolder.getNoRepoTextView().setText(noRepoItem.getMessage());
        } else if (holder instanceof RepoItemViewHolder) {
            Repo repo = (Repo) repoList.get(position);
            RepoItemViewHolder repoItemViewHolder = (RepoItemViewHolder) holder;
            repoItemViewHolder.getRepoNameTextView().setText(repo.getName());
            repoItemViewHolder.getFavoriteRepoImageView().setVisibility(repo.getIsFavorite()
                    ? View.VISIBLE
                    : View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return repoList == null ? 0 : repoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return VIEW_TYPE_HEADER;
        } else if (repoList.get(position) instanceof NoRepo) {
            return VIEW_TYPE_NO_REPO;
        } else {
            return super.getItemViewType(position);
        }
    }


    public class HeaderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatEditText editUserName;
        private Button repoSearchingButton;
        private String lastSearchedText = "";

        HeaderItemViewHolder(View itemView) {
            super(itemView);
            editUserName = itemView.findViewById(R.id.editUserName);
            repoSearchingButton = itemView.findViewById(R.id.repoSearchingButton);
            repoSearchingButton.setOnClickListener(this);
        }

        public AppCompatEditText getEditUserName() {
            return editUserName;
        }

        public Button getRepoSearchingButton() {
            return repoSearchingButton;
        }

        @Override
        public void onClick(View v) {
            if (null != repoSearchingInstructionListener) {
                Editable editUserNameText = editUserName.getText();
                if (!TextUtils.isEmpty(editUserNameText)) {
                    String newSearchString = editUserNameText.toString();
                    if (!lastSearchedText.equals(newSearchString)) {
                        lastSearchedText = newSearchString;
                        repoSearchingInstructionListener.onSubmitButtonClicked(lastSearchedText);
                    }
                } else {
                    editUserName.setError(itemView.getContext().getString(R.string.empty_search_warning));
                }
            }
        }
    }

    public class RepoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView repoNameTextView;
        private ImageView favoriteRepoImageView;

        RepoItemViewHolder(View itemView) {
            super(itemView);
            repoNameTextView = itemView.findViewById(R.id.repoNameTextView);
            favoriteRepoImageView = itemView.findViewById(R.id.favoriteRepoImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public ImageView getFavoriteRepoImageView() {
            return favoriteRepoImageView;
        }

        public void setFavoriteRepoImageView(ImageView favoriteRepoImageView) {
            this.favoriteRepoImageView = favoriteRepoImageView;
        }

        public TextView getRepoNameTextView() {
            return repoNameTextView;
        }

        public void setRepoNameTextView(TextView repoNameTextView) {
            this.repoNameTextView = repoNameTextView;
        }
    }

    public class NoRepoViewHolder extends RecyclerView.ViewHolder {

        private TextView noRepoTextView;

        NoRepoViewHolder(View itemView) {
            super(itemView);
            noRepoTextView = itemView.findViewById(R.id.noRepoTextView);
        }

        public TextView getNoRepoTextView() {
            return noRepoTextView;
        }
    }

    public RepoItemBase getItem(int id) {
        return repoList.get(id);
    }

    public void setRepoItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface RepoSearchingInstructionListener {
        void onSubmitButtonClicked(String searchText);
    }
}
