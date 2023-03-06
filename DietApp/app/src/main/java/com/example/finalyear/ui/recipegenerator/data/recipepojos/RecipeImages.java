package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeImages implements Parcelable {

	@SerializedName("THUMBNAIL")
	@Expose
	@Embedded(prefix = "thumb_")
	public THUMBNAIL tHUMBNAIL;

	public THUMBNAIL getTHUMBNAIL(){
		return tHUMBNAIL;
	}
	@SerializedName("REGULAR")
	@Expose
	@Embedded(prefix = "regular_")
	public REGULAR rRegular;

	public THUMBNAIL gettHUMBNAIL() {
		return tHUMBNAIL;
	}

	public REGULAR getrRegular() {
		return rRegular;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.tHUMBNAIL, flags);
		dest.writeParcelable(this.rRegular, flags);
	}

	public void readFromParcel(Parcel source) {
		this.tHUMBNAIL = source.readParcelable(THUMBNAIL.class.getClassLoader());
		this.rRegular = source.readParcelable(REGULAR.class.getClassLoader());
	}

	public RecipeImages() {
	}

	protected RecipeImages(Parcel in) {
		this.tHUMBNAIL = in.readParcelable(THUMBNAIL.class.getClassLoader());
		this.rRegular = in.readParcelable(REGULAR.class.getClassLoader());
	}

	public static final Creator<RecipeImages> CREATOR = new Creator<RecipeImages>() {
		@Override
		public RecipeImages createFromParcel(Parcel source) {
			return new RecipeImages(source);
		}

		@Override
		public RecipeImages[] newArray(int size) {
			return new RecipeImages[size];
		}
	};
}