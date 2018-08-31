package com.koowalk.shop.guis;

public enum GUITypeIdentifier {
	TYPE_IMAGE(0, "image"),
	TYPE_LABEL(1, "label"),
	TYPE_FRAME(2, "frame");
	
	public static final int TYPE_COUNT = 3;
	
	private int id;
	private String tag;
	
	private GUITypeIdentifier(int id, String tag) {
		this.id = id;
		this.tag = tag;
	}
	
	public int getIndex() {
		return id;
	}
	
	public String getTag() {
		return tag;
	}
}
