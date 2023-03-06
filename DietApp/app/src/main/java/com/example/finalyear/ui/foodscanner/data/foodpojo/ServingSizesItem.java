package com.example.finalyear.ui.foodscanner.data.foodpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServingSizesItem{
	@Expose
	@SerializedName("quantity")
	private Double quantity;
	@Expose
	@SerializedName("label")
	private String label;


	public Double getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}
}