package com.example.finalyear.ui.foodscanner.data.foodpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodResponse {
    @Expose
    @SerializedName("hints")
    private List<HintsItem> hints;

    @Expose
    @SerializedName("text")
    private String text;

    public List<HintsItem> getHints() {
        return hints;
    }

    public String getText() {
        return text;
    }

    public HintsItem getItem() {
        return hints == null ? null : hints.get(0);
    }

}