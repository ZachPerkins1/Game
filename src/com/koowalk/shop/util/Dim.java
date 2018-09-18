package com.koowalk.shop.util;

// Represents a dimension
// Used when a function needs to be multipurposed to serve more than just one dimension
public enum Dim {
	X,
	Y,
	Z;
	
	public static final Dim[] DIMENSIONS_2D = new Dim[] {X, Y};
	public static final Dim[] DIMENSIONS_3D = new Dim[] {X, Y, Z};
	
	public static <T> T switchDimXYZ(Dim d, T ifX, T ifY, T ifZ) {
		switch (d) {
		case X:
			return ifX;
		case Y:
			return ifY;
		case Z:
			return ifZ;
		default:
			throw new IllegalArgumentException("Dimension must be X, Y, or Z");
		}
	}
	
	public static <T> T switchDimXY(Dim d, T ifX, T ifY) {
		switch (d) {
		case X:
			return ifX;
		case Y:
			return ifY;
		default:
			throw new IllegalArgumentException("Dimension must be X or Y");
		}
	}
}
