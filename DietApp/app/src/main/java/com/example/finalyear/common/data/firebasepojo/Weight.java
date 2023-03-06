package com.example.finalyear.common.data.firebasepojo;

public class Weight{
	private int kg;
	private int gm;

	public Weight(){}
	public Weight(int kg, int gm) {
		this.kg = kg;
		this.gm = gm;
	}

	public int getGm(){
		return gm;
	}

	public int getKg(){
		return kg;
	}

}
