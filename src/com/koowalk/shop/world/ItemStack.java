package com.koowalk.shop.world;
import java.awt.Color;

import com.koowalk.shop.Statics;

public class ItemStack {
	private int item;
	private int count;
	
	public ItemStack(int item, int count) {
		this.item = item;
		this.count = count;
	}
	
	public ItemStack(int item) {
		this(item, 1);
	}
	
	public int getItem() {
		return item;
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean add(int amt) {
		int tot = count + amt;
		if (tot < 0) {
			return false;
		} else {
			count = tot;
			return true;
		}
		//TODO: Also check based on item size
	}
	
	public boolean remove(int amt) {
		return add(-amt);
	}
	
	public Color getTexture() {
		return Statics.TEX[Statics.ITM(item)];
	}
	
	public ItemInfo getInfo() {
		return Statics.IEN[item];
	}
}
