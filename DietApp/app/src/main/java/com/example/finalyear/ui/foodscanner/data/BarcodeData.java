package com.example.finalyear.ui.foodscanner.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class BarcodeData implements Parcelable {

    String text;
    Bitmap bitmap;

    public BarcodeData(String text, Bitmap bitmap) {
        this.text = text;
        this.bitmap = bitmap;
    }
    public BarcodeData(String text) {
        this.text = text;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeParcelable(this.bitmap, flags);
    }

    public void readFromParcel(Parcel source) {
        this.text = source.readString();
        this.bitmap = source.readParcelable(Bitmap.class.getClassLoader());
    }

    protected BarcodeData(Parcel in) {
        this.text = in.readString();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<BarcodeData> CREATOR = new Parcelable.Creator<BarcodeData>() {
        @Override
        public BarcodeData createFromParcel(Parcel source) {
            return new BarcodeData(source);
        }

        @Override
        public BarcodeData[] newArray(int size) {
            return new BarcodeData[size];
        }
    };
}
