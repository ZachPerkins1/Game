package com.koowalk.shop.util;

public abstract class SortedPoint2D implements Comparable<SortedPoint2D> {
	public abstract int getX();
	public abstract int getY();
	
	public int compareTo(SortedPoint2D o) {
		int x = getX();
		int y = getY();
		int ox = o.getX();
		int oy = o.getY();
		
		if (x == ox) {
			if (y == oy) {
				return 0;
			} else {
				return (y > oy)? 1: -1;
			}
		} else {
			return (x > ox)? 1: -1;
		}
	}
	
	public boolean equals(SortedPoint2D point) {
		return compareTo(point) == 0;
	}
}
