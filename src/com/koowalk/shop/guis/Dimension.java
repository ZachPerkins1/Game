package com.koowalk.shop.guis;

public abstract class Dimension {
	public abstract int get();
	
	public Dimension add(int c) {
		return Dimension.add(this, new SimpleDimension(c));
	}
	
	public static DimensionAdder add(Dimension a, Dimension b) {
		return new DimensionAdder(a, b);
	}
	
	public static DimensionMax max(Dimension a, Dimension b) {
		return new DimensionMax(a, b);
	}
	
	public static DimensionMin min(Dimension a, Dimension b) {
		return new DimensionMin(a, b);
	}
}
