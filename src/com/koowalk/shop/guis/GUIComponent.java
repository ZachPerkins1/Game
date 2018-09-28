package com.koowalk.shop.guis;

import java.awt.Rectangle;

import com.koowalk.shop.Window;
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
	
	protected boolean updated;
	
	public GUIComponent(GUITypeIdentifier type) {
		width = new DimensionMeasurement();
		height = new DimensionMeasurement();
		
		uid = uidCounter;
		uidCounter++;
		this.type = type;
	}
	
	public int getPaddedWidth() {
		return getWidth() + pLeft + pRight;
	}
	
	public int getPaddingX() {
		return pLeft + pRight;
	}
	
	public int getPaddedHeight() {
		return getHeight() + pTop + pBottom;
	}
	
	public int getPaddingY() {
		return pBottom + pTop;
	}
	
	public DimensionMeasurement getWidthMeasurement() {
		return width;
	}
	
	public DimensionMeasurement getHeightMeasurement() {
		return height;
	}
	
	public int getWidth() {
		return getDimensionMeasurementByDim(Dim.X).get();
	}
	
	public int getHeight() {
		return getDimensionMeasurementByDim(Dim.Y).get();
	}
	
	public int getPaddingByDim(Dim d) {
		if (d == Dim.X) {
			return getPaddingX();
		} else if (d == Dim.Y) {
			return getPaddingY();
		}
		
		return 0;
	}
	
	public int getDimensionByDim(Dim d) {
		return getDimensionMeasurementByDim(d).get();
	}
	
	public DimensionMeasurement getDimensionMeasurementByDim(Dim d) {
		if (d == Dim.X) {
			return getWidthMeasurement();
		} else if (d == Dim.Y) {
			return getHeightMeasurement();
		}
		
		return null;
	}
	
	public Dimension getPaddedDimension(Dim d) {
		return getDimensionMeasurementByDim(d).add(getPaddingByDim(d));
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
		updated = top != pTop || bottom != pBottom || left != pLeft || right != pRight;
		pTop = top;
		pBottom = bottom;
		pLeft = left;
		pRight = right;
	}
	
	public void setPadding(int p) {
		setPadding(p, p, p, p);
	}
	
	public void setPaddingY(int top, int bottom) {
		setPadding(top, bottom, pLeft, pRight);
	}
	
	public void setPaddingX(int left, int right) {
		setPadding(pTop, pBottom, left, right);
	}
	
	public void setPaddingTop(int top) {
		setPadding(top, pBottom, pLeft, pRight);
	}
	
	public void setPaddingBottom(int bottom) {
		setPadding(pTop, bottom, pLeft, pRight);
	}
	
	public void setPaddingLeft(int left) {
		setPadding(pTop, pBottom, left, pRight);
	}
	
	public void setPaddingRight(int right) {
		setPadding(pTop, pBottom, pLeft, right);
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
	
	public boolean hasBeenUpdated() {
		return this.popUpdate() || width.popUpdate() || height.popUpdate();
	}
	
	public boolean popUpdate() {
		boolean old = updated;
		updated = false;
		return old;
	}
	
	public Rectangle getMaxBounds() {
		if (parent != null) {
			int width = parent.getWidthMeasurement().get();
			int height = parent.getHeightMeasurement().get();
			int x = parent.getPaddedX();
			int y = parent.getPaddedY();
			return new Rectangle(x, y, width, height);
		}
		
		return new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
	}
	
	public void update() {};
	public void place() {};
}
