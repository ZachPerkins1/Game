package com.koowalk.shop.util;

public class Container<T> {
	private T item;
	
	public void set(T item) {
		this.item = item;
	}
	
	public T get() {
		return item;
	}
}
