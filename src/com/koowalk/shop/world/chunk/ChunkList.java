package com.koowalk.shop.world.chunk;

import java.util.ArrayList;

import com.koowalk.shop.util.Point2D;
import com.koowalk.shop.util.SortedArrayList;
import com.koowalk.shop.util.SortedPoint2D;

public class ChunkList extends SortedArrayList<SortedPoint2D> {
	public Chunk at(int x, int y) {
		return (Chunk) find(new Point2D(x, y));
	}
	
	public boolean add(Chunk c) {		
		super.addSorted(c);
		return true;
	}
}
