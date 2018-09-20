package com.koowalk.shop.guis;

public class DimensionSubtractor extends DimensionComparator {

	public DimensionSubtractor(Dimension a, Dimension b) {
		super(a, b);
	}

	@Override
	public int compare(int a, int b) {	
		return a - b;
	}

}
