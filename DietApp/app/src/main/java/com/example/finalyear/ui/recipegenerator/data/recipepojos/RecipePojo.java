package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipePojo {

	@SerializedName("hits")
	@Expose
	private List<Recipe> recipeList;

	@SerializedName("_links")
	@Expose
	private Links links;

	@SerializedName("count")
	@Expose
	private int count;

	@SerializedName("from")
	@Expose
	private int from;

	@SerializedName("to")
	@Expose
	private int to;

	public List<Recipe> getRecipeList(){
		return recipeList;
	}

	public Links getLinks(){
		return links;
	}

	public int getCount(){
		return count;
	}

	public int getFrom(){
		return from;
	}

	public int getTo(){
		return to;
	}

	@Override
	public String toString() {
		return "RecipePojo{" +
				"recipeList=" + recipeList +
				", links=" + links +
				", count=" + count +
				", from=" + from +
				", to=" + to +
				'}';
	}
}