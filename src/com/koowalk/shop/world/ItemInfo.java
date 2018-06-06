package com.koowalk.shop.world;

public abstract class ItemInfo {
	public final int _ID;
	
	public ItemInfo(int id) {
		_ID = id;
	}
	
	public abstract int getSize();
}
