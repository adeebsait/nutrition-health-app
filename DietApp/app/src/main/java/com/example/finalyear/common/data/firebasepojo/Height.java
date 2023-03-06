package com.example.finalyear.common.data.firebasepojo;

public class Height{
	private int in;
	private int ft;

	public int getIn(){
		return in;
	}

	public int getFt(){
		return ft;
	}

	public Height(){}
	public Height( int ft,int in) {
		this.in = in;
		this.ft = ft;
	}
}
