package com.docotel.latihanmvp2;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.docotel.latihanmvp2.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Context mContext;
    private List<Photo> photos;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView.Adapter mAdapter;

    public PhotoAdapter(RecyclerView recyclerView, List<Photo> photos, Context mContext, final LinearLayoutManager linearLayoutManager) {
        this.photos = photos;
        this.mContext = mContext;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return photos.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.main_item, parent, false);
            return new UserViewHolder(view,this);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            Photo photo = photos.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.albumId.setText(photo.getAlbumId());
            userViewHolder.Id.setText(photo.getId());
            userViewHolder.Title.setText(photo.getTitle());
            userViewHolder.Url.setText(photo.getUrl());
            Glide.with(mContext).load(photo.getThumbnailUrl()).into(userViewHolder.ThumbnailUrl);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public Photo getItem(int position) {
        return photos.get(position);
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView albumId;
        private TextView Id;
        private TextView Title;
        private TextView Url;
        private ImageView ThumbnailUrl;

        private UserViewHolder(View view, RecyclerView.Adapter adapter) {
            super(view);

            mAdapter = adapter;

            albumId = view.findViewById(R.id.tvAlbumId);
            Id = view.findViewById(R.id.tvId);
            Title = view.findViewById(R.id.tvTitle);
            Url = view.findViewById(R.id.tvUrl);
            ThumbnailUrl = view.findViewById(R.id.ivThumb);
        }

    }
}
