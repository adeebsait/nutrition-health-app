package com.example.finalyear.ui.foodscanner.data.foodpojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HintsItem{
	@Expose
	@SerializedName("food")
	private Food food;


	public Food getFood(){
		return food;
	}
}