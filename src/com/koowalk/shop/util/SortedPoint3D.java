package com.koowalk.shop.util;

public abstract class SortedPoint3D extends SortedPoint2D {
	public abstract int getZ();
	
	public int compareTo(SortedPoint3D o) {
		int z = getZ();
		int oz = o.getZ();
		
		int result = super.compareTo(o);
		if (result == 0) {
			if (z == oz) {
				return 0;
			} else {
				return (z > oz)? 1: -1;
			}
		} else {
			return result;
		}
	}
}
