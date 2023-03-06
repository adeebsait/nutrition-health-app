package com.example.finalyear.ui.inventory.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.finalyear.ui.foodscanner.data.ScanHistoryItem;

import java.util.Objects;

public class InventoryItem implements Parcelable {
    private String id;
    private String name;
    private Integer quantity;
    private String quantityUnit;
    private String sugar;
    private Long expiry = System.currentTimeMillis();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getExpiry() {
        return expiry;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public InventoryItem() {
    }

    public InventoryItem(String id, String name, Integer quantity, String quantityUnit,String sugar, Long expiry) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.sugar = sugar;
        this.expiry = expiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem item = (InventoryItem) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(quantity, item.quantity) && Objects.equals(quantityUnit, item.quantityUnit) && Objects.equals(expiry, item.expiry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, quantityUnit, expiry);
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", quantityUnit='" + quantityUnit + '\'' +
                ", expiry=" + expiry +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.quantity);
        dest.writeString(this.quantityUnit);
        dest.writeValue(this.expiry);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.quantity = (Integer) source.readValue(Integer.class.getClassLoader());
        this.quantityUnit = source.readString();
        this.expiry = (Long) source.readValue(Long.class.getClassLoader());
    }

    protected InventoryItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantityUnit = in.readString();
        this.expiry = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<InventoryItem> CREATOR = new Parcelable.Creator<InventoryItem>() {
        @Override
        public InventoryItem createFromParcel(Parcel source) {
            return new InventoryItem(source);
        }

        @Override
        public InventoryItem[] newArray(int size) {
            return new InventoryItem[size];
        }
    };
}
