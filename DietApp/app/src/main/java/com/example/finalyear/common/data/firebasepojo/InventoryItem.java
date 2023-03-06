package com.example.finalyear.common.data.firebasepojo;

public class InventoryItem{
	private int quantity;
	private String name;
	private String quantityUnit;
	private long expiry;
	private String id;

	public int getQuantity(){
		return quantity;
	}

	public String getName(){
		return name;
	}

	public String getQuantityUnit(){
		return quantityUnit;
	}

	public long getExpiry(){
		return expiry;
	}

	public String getId(){
		return id;
	}
}
