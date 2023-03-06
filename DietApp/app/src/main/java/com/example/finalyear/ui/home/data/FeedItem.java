package com.example.finalyear.ui.home.data;

import com.google.gson.annotations.SerializedName;

public class FeedItem {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String imgUrl;
    @SerializedName("url")
    private String redirectUrl;
    @SerializedName("post_type")
    private int viewType;

    public String getTitle() {
        return title;
    }

    public int getViewType() {
        return viewType;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
