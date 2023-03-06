package com.example.finalyear.ui.recipegenerator.data.recipepojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class THUMBNAIL implements Parcelable {

	@SerializedName("url")
	@Expose
	private String url;

	public String getUrl(){
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public THUMBNAIL() {
	}

	protected THUMBNAIL(Parcel in) {
		this.url = in.readString();
	}

	public static final Parcelable.Creator<THUMBNAIL> CREATOR = new Parcelable.Creator<THUMBNAIL>() {
		@Override
		public THUMBNAIL createFromParcel(Parcel source) {
			return new THUMBNAIL(source);
		}

		@Override
		public THUMBNAIL[] newArray(int size) {
			return new THUMBNAIL[size];
		}
	};
}