package com.koowalk.shop.guis;

public interface Dimension {
	public int get();
	
	public static DimensionMax max(Dimension a, Dimension b) {
		return new DimensionMax(a, b);
	}
	
	public static DimensionMin min(Dimension a, Dimension b) {
		return new DimensionMin(a, b);
	}
}
