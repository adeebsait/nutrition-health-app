package com.example.finalyear.ui.foodscanner.data.foodpojo;

import com.example.finalyear.common.utils.Utility;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sugar {
    @Expose
    @SerializedName("SUGAR")
    private double sugar;

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public String getSugarRounded(){
        return Utility.getMilesString((float) sugar);
    }
}
