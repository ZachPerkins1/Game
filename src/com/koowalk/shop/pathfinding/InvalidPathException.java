package com.koowalk.shop.pathfinding;
import java.awt.Point;

public class InvalidPathException extends Exception {
	public InvalidPathException() {
		super("Path cannot be created");
	}
	
	public InvalidPathException(Point p) {
		super("Block is solid: " + p.toString());
	}
}