package com.koowalk.shop.guis;

public abstract class GUILayoutComponent implements GUIGLDrawable {
	private static int uidCounter = 0;
	private GUILayoutSettings settings;
	private long uid;
	
	public int x;
	public int y;
	
	public abstract int _getWidth();
	public abstract int _getHeight();
	
	public GUILayoutComponent() {
		uid = uidCounter;
		uidCounter++;
	}
	
	public GUILayoutSettings getLayoutSettings() {
		return settings;
	}
	
	public void setLayoutSettings(GUILayoutSettings settings) {
		this.settings = settings;
	}
	
	public boolean equals(GUIGLDrawable other) {
		return this.uid == other.getUID();
	}
	
	public long getUID() {
		return uid;
	}
}
