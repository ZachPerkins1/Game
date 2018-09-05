package com.koowalk.shop.guis;

import java.util.HashMap;

public abstract class GUIComponent extends GUILayoutComponent {
	private GUIComponent parent = null;
	private int width;
	private int height;
	
	private int pLeft;
	private int pRight;
	private int pTop;
	private int pBottom;
	
	private GUITypeIdentifier type;
	
	public GUIComponent(GUITypeIdentifier type) {
		this.type = type;
	}
	
	public int _getWidth() {
		if (width > 0) {
			return width;
		}
		
		return getWidth();
	}
	
	public int _getHeight() {
		if (height > 0) {
			return height;
		}
		
		return getHeight();
	}
	
	public int getPaddedWidth() {
		return _getWidth() + pLeft + pRight;
	}
	
	public int getPaddedHeight() {
		return _getHeight() + pTop + pBottom;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int getX() {
		if (parent != null) {
			return x + parent.getPaddedX();
		}
		
		return x;
	}
	
	public int getY() {
		if (parent != null) {
			return y + parent.getPaddedY();
		}
		
		return y;
	}
	
	public int getPaddedX() {
		int padX = x + pLeft;
		
		if (parent != null)
			padX += parent.getPaddedX();
		
		return padX;
	}
	
	public int getPaddedY() {
		int padY = y + pTop;
		
		if (parent != null)
			padY += parent.getPaddedY();
		
		return padY;
	}
	
	public void setPadding(int top, int bottom, int left, int right) {
		pTop = top;
		pBottom = bottom;
		pLeft = left;
		pRight = right;
	}
	
	public void setPadding(int p) {
		setPadding(p, p, p, p);
	}
	
	public void setPaddingY(int top, int bottom) {
		pTop = top;
		pBottom = bottom;
	}
	
	public void setPaddingX(int left, int right) {
		pLeft = left;
		pRight = right;
	}
	
	public void setPaddingTop(int top) {
		pTop = top;
	}
	
	public void setPaddingBottom(int bottom) {
		pBottom = bottom;
	}
	
	public void setPaddingLeft(int left) {
		pLeft = left;
	}
	
	public void setPaddingRight(int right) {
		pRight = right;
	}
	
	public GUITypeIdentifier getType() {
		return type;
	}
	
	public GUIComponent getParent() {
		return parent;
	}
	
	public void setParent(GUIFrame parent, GUILayoutSettings dict) {
		parent.add(this, dict);
		this.parent = parent;
	}
	
	public void update() {};
}
