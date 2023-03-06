package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


public class Recipe {

	@SerializedName("_links")
	@Expose
	private Links links;

	@SerializedName("recipe")
	@Expose
	private RecipeDetails recipeDetails;

	public Links getLinks(){
		return links;
	}

	public RecipeDetails getRecipe(){
		return recipeDetails;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Recipe recipe = (Recipe) o;
		return Objects.equals(links, recipe.links) && Objects.equals(recipeDetails, recipe.recipeDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(links, recipeDetails);
	}

	@Override
	public String toString() {
		return "Recipe{" +
				"links=" + links +
				", recipeDetails=" + recipeDetails +
				'}';
	}
}