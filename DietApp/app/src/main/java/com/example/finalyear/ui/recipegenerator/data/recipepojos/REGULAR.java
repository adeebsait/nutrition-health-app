package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class REGULAR implements Parcelable {

	@SerializedName("url")
	@Expose
	public String url;

	public String getUrl() {
		return url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
	}

	public void readFromParcel(Parcel source) {
		this.url = source.readString();
	}

	public REGULAR() {
	}

	protected REGULAR(Parcel in) {
		this.url = in.readString();
	}

	public static final Creator<REGULAR> CREATOR = new Creator<REGULAR>() {
		@Override
		public REGULAR createFromParcel(Parcel source) {
			return new REGULAR(source);
		}

		@Override
		public REGULAR[] newArray(int size) {
			return new REGULAR[size];
		}
	};
}