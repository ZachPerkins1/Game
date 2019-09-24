package com.koowalk.shop.util;

public abstract class Event {
	private String message;
	public Event(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message;
	}
	
	public abstract boolean checkPeg(Object o);
}
