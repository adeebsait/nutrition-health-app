package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

@Entity(tableName = "recipes")
public class RecipeDetails implements Parcelable {

    @PrimaryKey
    @SerializedName("uri")
    @NonNull
    @Expose
    public String id = ""+System.currentTimeMillis();

    @SerializedName("label")
    @Expose
    public String recipeName;

    @SerializedName("calories")
    @Expose
    public double calories;

    @SerializedName("images")
    @Expose
    @Embedded
    public RecipeImages images;
    @SerializedName("ingredients")
    @Expose
    public List<RecipeIngredients> ingredients;

	@NonNull
    public String getId() {
		return id;
	}

    public String getRecipeName() {
        return recipeName;
    }

    public RecipeImages getImages() {
        return images;
    }

    public double getCalories() {
        return calories;
    }

	public List<RecipeIngredients> getIngredients() {
		return ingredients;
	}

    public void setId(String id) {
        this.id = id;
    }

    public RecipeDetails() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDetails details = (RecipeDetails) o;
        return Double.compare(details.calories, calories) == 0 && Objects.equals(recipeName, details.recipeName) && Objects.equals(images, details.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeName, calories, images);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recipeName);
        dest.writeDouble(this.calories);
        dest.writeParcelable(this.images, flags);
        dest.writeList(this.ingredients);
    }

    public void readFromParcel(Parcel source) {
        this.recipeName = source.readString();
        this.calories = source.readDouble();
        this.images = source.readParcelable(RecipeImages.class.getClassLoader());
        this.ingredients = new ArrayList<RecipeIngredients>();
        source.readList(this.ingredients, RecipeIngredients.class.getClassLoader());
    }

    protected RecipeDetails(Parcel in) {
        this.recipeName = in.readString();
        this.calories = in.readDouble();
        this.images = in.readParcelable(RecipeImages.class.getClassLoader());
        this.ingredients = new ArrayList<RecipeIngredients>();
        in.readList(this.ingredients, RecipeIngredients.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecipeDetails> CREATOR = new Parcelable.Creator<RecipeDetails>() {
        @Override
        public RecipeDetails createFromParcel(Parcel source) {
            return new RecipeDetails(source);
        }

        @Override
        public RecipeDetails[] newArray(int size) {
            return new RecipeDetails[size];
        }
    };

    @Override
    public String toString() {
        return "RecipeDetails{" +
                "id='" + id + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", calories=" + calories +
                ", images=" + images +
                ", ingredients=" + ingredients +
                '}';
    }
}