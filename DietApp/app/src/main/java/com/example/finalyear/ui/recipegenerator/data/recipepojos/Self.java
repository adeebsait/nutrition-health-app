package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import com.google.gson.annotations.SerializedName;

public class Self {

	@SerializedName("href")
	private String href;

	@SerializedName("title")
	private String title;

	public String getHref(){
		return href;
	}

	public String getTitle(){
		return title;
	}
}