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
	
	public Block clone() {
		return new Block(id, subID);
	}
	
	public boolean equals(Block b) {
		return b.id == id && b.subID == subID;
	}
	
	public boolean isAir() {
		return id == 0;
	}
	
	public String toString() {
		return "id: " + id + "sub_id: " + subID;
	}
}
