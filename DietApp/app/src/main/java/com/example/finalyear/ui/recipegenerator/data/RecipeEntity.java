package com.example.finalyear.ui.recipegenerator.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class RecipeEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    String title;
    String description;

    String thumbnail;

    Integer calories;

    public RecipeEntity(String title, String description, String thumbnail,Integer calories) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.calories = calories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeValue(this.calories);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.title = source.readString();
        this.description = source.readString();
        this.calories = (Integer) source.readValue(Integer.class.getClassLoader());
    }

    protected RecipeEntity(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
        this.calories = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecipeEntity> CREATOR = new Parcelable.Creator<RecipeEntity>() {
        @Override
        public RecipeEntity createFromParcel(Parcel source) {
            return new RecipeEntity(source);
        }

        @Override
        public RecipeEntity[] newArray(int size) {
            return new RecipeEntity[size];
        }
    };
}
