package com.koowalk.shop.world.chunk;

import com.koowalk.shop.util.SortedPoint3D;

public class BlockInfo extends SortedPoint3D {
	private Block b;
	private int x;
	private int y;
	private int layer;
		
	public BlockInfo(Block b, int x, int y, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
		this.b = b;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public Block getBlock() {
		return b;
	}
	
	public void update(Block b) {
		this.b = b.clone();
	}

	@Override
	public int getZ() {
		return layer;
	}
	
	public boolean equals(BlockInfo b) {
		return this.b.equals(b.b) && super.equals(b);
	}
	
	public String toString() {
		return "[" + b.toString() + "] @ (" + x + ", " + y + ", " + layer + ")";
	}
}
