package com.koowalk.shop.world.chunk;

public class Block {
	private int id;
	private int subID;
	
	public Block(int id, int subID) { 
		this.id = id;
		this.subID = subID;
	}
	
	public Block(int id) {
		this(id, 0);
	}
	
	public int getID() {
		return id;
	}
	
	public int getSubID() {
		return subID;
	}
}
