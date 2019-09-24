package com.koowalk.shop.util;

public class Pair<T, K> {
	T p0;
	K p1;
	
	public Pair(T p0, K p1) {
		this.p0 = p0;
		this.p1 = p1;
	}
	
	public T getP0() {
		return p0;
	}
	
	public K getP1() {
		return p1;
	}
}
