package com.koowalk.shop.guis.dimension;

public class DimensionAdder extends DimensionComparator {

	public DimensionAdder(Dimension a, Dimension b) {
		super(a, b);
	}

	@Override
	public int compare(int a, int b) {
		return a + b;
	}
	
}
