package com.koowalk.shop.util;

public class Container<T> {
	private T item;
	
	public Container(T initial) {
		item = initial;
	}
	
	public Container() {
		this(null);
	}
	
	public void set(T item) {
		this.item = item;
	}
	
	public T get() {
		return item;
	}
	
	public String toString() {
		return item.toString();
	}
}
