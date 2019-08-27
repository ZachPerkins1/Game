package com.koowalk.shop.guis.dimension;

public abstract class Dimension {
	public abstract int get();
	
	public Dimension add(int c) {
		return Dimension.add(this, new SimpleDimension(c));
	}
	
	public String toString() {
		return "Dimension [size = " + get() + "]";
	}
	
	public static DimensionAdder add(Dimension a, Dimension b) {
		return new DimensionAdder(a, b);
	}
	
	public static DimensionSubtractor subtract(Dimension a, Dimension b) {
		return new DimensionSubtractor(a, b);
	}
	
	public static DimensionMax max(Dimension a, Dimension b) {
		return new DimensionMax(a, b);
	}
	
	public static DimensionMin min(Dimension a, Dimension b) {
		return new DimensionMin(a, b);
	}
}
