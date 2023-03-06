package com.example.finalyear.ui.foodscanner.data.foodpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Food {
    @Expose
    @SerializedName("servingSizes")
    private List<ServingSizesItem> servingSizes;
    @Expose
    @SerializedName("label")
    private String label;
    @Expose
    @SerializedName("category")
    private String category;
    @Expose
    @SerializedName("nutrients")
    private Sugar nutrients;

    public List<ServingSizesItem> getServingSizes() {
        return servingSizes;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

    public Sugar getNutrients() {
        return nutrients;
    }

    public ServingSizesItem getFirstSize() {
        return servingSizes != null ? servingSizes.get(0) : null;
    }
}