package com.koowalk.shop.world.chunk;

import java.util.ArrayList;

import com.koowalk.shop.util.Point3D;
import com.koowalk.shop.util.SortedArrayList;
import com.koowalk.shop.util.SortedPoint2D;
import com.koowalk.shop.util.SortedPoint3D;

public class ChunkInfo extends SortedPoint2D {
	private SortedArrayList<SortedPoint3D> blocks;
	
	private int cx;
	private int cy;
	
	public ChunkInfo(int x, int y) {
		blocks = new SortedArrayList<SortedPoint3D>();
		cx = x;
		cy = y;
	}
	
	public ArrayList<SortedPoint3D> getBlocks() {
		return blocks;
	}
	
	public void setBlock(int x, int y, int layer, Block b) {
		BlockInfo block = getBlock(x, y, layer);
		if (block != null) {
			block.update(b);
		} else {
			add(new BlockInfo(b, x, y, layer));
		}
	}
	
	public BlockInfo getBlock(int x, int y, int layer) {
		return (BlockInfo) blocks.find(new Point3D(x, y, layer));
	}
	
	public BlockInfo get(int i) {
		return (BlockInfo) blocks.get(i);
	} 
	
	public void add(BlockInfo f) {
		blocks.addSorted(f);
	}
	
	public void remove(BlockInfo b) {
		while(blocks.remove(b));
	}
	
	public int getBlockCount() {
		return blocks.size();
	}
	
	public int getX() {
		return cx;
	}
	
	public int getY() {
		return cy;
	}
	
	public String toString() {
		return blocks.toString();
	}
}
