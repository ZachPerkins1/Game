package com.koowalk.shop.guis;

public interface GUIGLDrawable {
	public int _getWidth();
	public int _getHeight();
	public int getX();
	public int getY();
	public boolean equals(GUIGLDrawable other);
	public long getUID();
}