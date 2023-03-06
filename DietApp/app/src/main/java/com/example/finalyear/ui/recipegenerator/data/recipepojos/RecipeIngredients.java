package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RecipeIngredients implements Parcelable {

	@SerializedName("image")
	@Expose
	public String image;

	@SerializedName("quantity")
	@Expose
	public double quantity;

	@SerializedName("measure")
	@Expose
	public String measure;

	@SerializedName("foodId")
	@Expose
	public String foodId;

	@SerializedName("weight")
	@Expose
	public double weight;

	@SerializedName("text")
	@Expose
	public String text;

	@SerializedName("foodCategory")
	@Expose
	public String foodCategory;

	@SerializedName("food")
	@Expose
	public String food;

	public String getImage(){
		return image;
	}

	public double getQuantity(){
		return quantity;
	}

	public String getMeasure(){
		return measure;
	}

	public String getFoodId(){
		return foodId;
	}

	public double getWeight(){
		return weight;
	}

	public String getText(){
		return text;
	}

	public String getFoodCategory(){
		return foodCategory;
	}

	public String getFood(){
		return food;
	}


	@Override
	public String toString() {
		return "RecipeIngredients{" +
				"image='" + image + '\'' +
				", quantity=" + quantity +
				", measure='" + measure + '\'' +
				", foodId='" + foodId + '\'' +
				", weight=" + weight +
				", text='" + text + '\'' +
				", foodCategory='" + foodCategory + '\'' +
				", food='" + food + '\'' +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.image);
		dest.writeDouble(this.quantity);
		dest.writeString(this.measure);
		dest.writeString(this.foodId);
		dest.writeDouble(this.weight);
		dest.writeString(this.text);
		dest.writeString(this.foodCategory);
		dest.writeString(this.food);
	}

	public void readFromParcel(Parcel source) {
		this.image = source.readString();
		this.quantity = source.readDouble();
		this.measure = source.readString();
		this.foodId = source.readString();
		this.weight = source.readDouble();
		this.text = source.readString();
		this.foodCategory = source.readString();
		this.food = source.readString();
	}

	public RecipeIngredients() {
	}

	protected RecipeIngredients(Parcel in) {
		this.image = in.readString();
		this.quantity = in.readDouble();
		this.measure = in.readString();
		this.foodId = in.readString();
		this.weight = in.readDouble();
		this.text = in.readString();
		this.foodCategory = in.readString();
		this.food = in.readString();
	}

	public static final Parcelable.Creator<RecipeIngredients> CREATOR = new Parcelable.Creator<RecipeIngredients>() {
		@Override
		public RecipeIngredients createFromParcel(Parcel source) {
			return new RecipeIngredients(source);
		}

		@Override
		public RecipeIngredients[] newArray(int size) {
			return new RecipeIngredients[size];
		}
	};
}