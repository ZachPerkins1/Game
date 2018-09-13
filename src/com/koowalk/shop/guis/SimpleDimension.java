package com.koowalk.shop.guis;

public class SimpleDimension extends Dimension {
	private int amount;
	
	public SimpleDimension(int amount) {
		this.amount = amount;
	}
	
	@Override
	public int get() {
		return amount;
	}
}
