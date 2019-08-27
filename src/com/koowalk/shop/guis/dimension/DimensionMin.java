package com.koowalk.shop.guis.dimension;

public class DimensionMin extends DimensionComparator {

	public DimensionMin(Dimension a, Dimension b) {
		super(a, b);
	}

	@Override
	public int compare(int a, int b) {
		return Math.min(a, b);
	}

}
