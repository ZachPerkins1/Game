package com.koowalk.shop.guis.dimension;

public abstract class DimensionComparator extends Dimension {
	private Dimension a;
	private Dimension b;
	
	public DimensionComparator(Dimension a, Dimension b) {
		this.a = a;
		this.b = b;
	}
	
	public int get() {
		return compare(a.get(), b.get());
	}
	
	public abstract int compare(int a, int b);
}
