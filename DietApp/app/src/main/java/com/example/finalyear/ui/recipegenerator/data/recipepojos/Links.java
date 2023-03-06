package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links{

	@SerializedName("_links")
	@Expose
	private Links links;

	@SerializedName("self")
	@Expose
	private Self self;

	@SerializedName("next")
	@Expose
	private Next next;

	public Links getLinks(){
		return links;
	}

	public Next getNext(){
		return next;
	}

	public Self getSelf() {
		return self;
	}



}