package com.koowalk.shop.guis;

import com.koowalk.shop.util.Dim;

public abstract class GUIComponent {
	private GUIFrame parent = null;
	
	private int pLeft;
	private int pRight;
	private int pTop;
	private int pBottom;
	
	private static int uidCounter = 0;
	private GUILayoutSettings settings;
	private long uid;
	
	public int x;
	public int y;
	
	private GUITypeIdentifier type;
	
	private DimensionMeasurement width;
	private DimensionMeasurement height;
	
	public GUIComponent(GUITypeIdentifier type) {
		uid = uidCounter;
		uidCounter++;
		this.type = type;
	}
	
	public int getPaddedWidth() {
		return getWidth() + pLeft + pRight;
	}
	
	public int getPaddedHeight() {
		return getHeight() + pTop + pBottom;
	}
	
	public int getDimensionByDim(Dim d) {
		if (d == Dim.X) {
			return getWidth();
		} else if (d == Dim.Y) {
			return getHeight();
		}
		
		return 0;
	}
	
	public DimensionMeasurement getDimensionMeasurementByDim(Dim d) {
		if (d == Dim.X) {
			return width;
		} else if (d == Dim.Y) {
			return height;
		}
		
		return null;
	}
	
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
	
	public GUIFrame getParent() {
		return parent;
	}
	
	public void setParent(GUIFrame parent, GUILayoutSettings dict) {
		parent.add(this, dict);
		this.parent = parent;
	}
	
	public GUILayoutSettings getLayoutSettings() {
		return settings;
	}
	
	public void setLayoutSettings(GUILayoutSettings settings) {
		this.settings = settings;
	}
	
	public boolean equals(GUIComponent other) {
		return this.uid == other.getUID();
	}
	
	public long getUID() {
		return uid;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	public void update() {};
}
