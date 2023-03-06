package com.example.finalyear.ui.recipegenerator.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalyear.ui.inventory.data.InventoryItem;

import java.util.List;

public class InventoryItemListPojo implements Parcelable {
    public List<InventoryItem> inventoryItemList;

    public List<InventoryItem> getInventoryItemList() {
        return inventoryItemList;
    }


    public InventoryItemListPojo() {
    }


    public InventoryItemListPojo(List<InventoryItem> inventoryItemList) {
        this.inventoryItemList = inventoryItemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.inventoryItemList);
    }

    public void readFromParcel(Parcel source) {
        this.inventoryItemList = source.createTypedArrayList(InventoryItem.CREATOR);
    }


    protected InventoryItemListPojo(Parcel in) {
        this.inventoryItemList = in.createTypedArrayList(InventoryItem.CREATOR);
    }

    public static final Parcelable.Creator<InventoryItemListPojo> CREATOR = new Parcelable.Creator<InventoryItemListPojo>() {
        @Override
        public InventoryItemListPojo createFromParcel(Parcel source) {
            return new InventoryItemListPojo(source);
        }

        @Override
        public InventoryItemListPojo[] newArray(int size) {
            return new InventoryItemListPojo[size];
        }
    };
}
