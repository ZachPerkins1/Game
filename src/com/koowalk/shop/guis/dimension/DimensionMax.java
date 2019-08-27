package com.koowalk.shop.guis.dimension;

public class DimensionMax extends DimensionComparator {

	public DimensionMax(Dimension a, Dimension b) {
		super(a, b);
	}

	@Override
	public int compare(int a, int b) {
		return Math.max(a, b);
	}

}
