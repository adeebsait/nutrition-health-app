package com.example.finalyear.ui.home.presentation.feed;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyear.databinding.ItemFeedImageBinding;
import com.example.finalyear.databinding.ItemFeedPostBinding;
import com.example.finalyear.databinding.ItemFeedVideoBinding;
import com.example.finalyear.ui.home.data.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_POST = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private final List<FeedItem> items = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_POST:
                return new PostViewHolder(
                        ItemFeedPostBinding.inflate(LayoutInflater
                                .from(parent.getContext()), parent, false)
                );
            case TYPE_IMAGE:
                return new ImageViewHolder(
                        ItemFeedImageBinding.inflate(LayoutInflater
                                .from(parent.getContext()), parent, false)
                );
            case TYPE_VIDEO:
                return new VideoViewHolder(
                        ItemFeedVideoBinding.inflate(LayoutInflater
                                .from(parent.getContext()), parent, false)
                );

        }
        return new PostViewHolder(
                ItemFeedPostBinding.inflate(LayoutInflater
                        .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_POST:
                ((PostViewHolder)holder).bind(items.get(position));
                break;
            case TYPE_IMAGE:
                ((ImageViewHolder)holder).bind(items.get(position));
                break;
            case TYPE_VIDEO:
                ((VideoViewHolder)holder).bind(items.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        FeedItem item = items.get(position);
        if (item.getViewType() == 1) {
            return TYPE_IMAGE;
        } else if (item.getViewType() == 2) {
            return TYPE_VIDEO;
        } else {
            return TYPE_POST;
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeedPostBinding binding;
        public PostViewHolder(@NonNull ItemFeedPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeedItem feedItem) {
            binding.titleTv.setText(feedItem.getTitle());
            binding.descTv.setText(feedItem.getDescription());
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeedImageBinding binding;
        public ImageViewHolder(@NonNull ItemFeedImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(FeedItem feedItem) {
            binding.titleTv.setText(feedItem.getTitle());
            Glide.with(itemView).load(feedItem.getImgUrl()).into(binding.thumbnaimIV);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeedVideoBinding binding;
        public VideoViewHolder(@NonNull ItemFeedVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeedItem feedItem) {
            binding.titleTv.setText(feedItem.getTitle());
            Glide.with(itemView).load(feedItem.getImgUrl()).into(binding.thumbnaimIV);
            binding.playBtn.setOnClickListener(v -> {

            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<FeedItem> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

}
