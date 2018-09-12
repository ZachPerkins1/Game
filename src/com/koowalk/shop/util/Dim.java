package com.koowalk.shop.util;

// Represents a dimension
// Used when a function needs to be multipurposed to serve more than just one dimension
public enum Dim {
	X,
	Y,
	Z;
	
	public static final Dim[] DIMENSIONS_2D = new Dim[] {X, Y};
	public static final Dim[] DIMENSIONS_3D = new Dim[] {X, Y, Z};
}
