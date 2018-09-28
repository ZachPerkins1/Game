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
	
	
	/**
	 * Get the DimensionMeasurement in the specified dimension.
	 * @param d The dimension.
	 * @return The DimensionMeasurement
	 */
	public DimensionMeasurement getDimensionMeasurementByDim(Dim d) {
		if (d == Dim.X) {
			return getWidthMeasurement();
		} else if (d == Dim.Y) {
			return getHeightMeasurement();
		}
		
		return null;
	}
	
	/**
	 * Get the padded width/height (i.e. regular height/width + paddingx/paddingy)
	 * @param d The dimension to use
	 * @return The dimension that represents this padded dimension. This will update with this component's width/height
	 */
	public Dimension getPaddedDimension(Dim d) {
		return getDimensionMeasurementByDim(d).add(getPaddingByDim(d));
	}
	
	/**
	 * Get the x position of this element calculated as a sum of this element's own position and it's parent's
	 * position
	 * @return The x of this element relative to the upper left corner of the window
	 */
	public int getX() {
		if (parent != null) {
			return x + parent.getPaddedX();
		}
		
		return x;
	}	
	/**
	 * Get the y position of this element calculated as a sum of this element's own position and it's parent's
	 * position
	 * @return The y of this element relative to the upper left corner of the window
	 */
	public int getY() {
		if (parent != null) {
			return y + parent.getPaddedY();
		}
		
		return y;
	}
	
	/**
	 * Get the x position of this component plus the left padding
	 * @return The "padded" y of this GUIComponent
	 */
	public int getPaddedX() {
		int padX = x + pLeft;
		
		if (parent != null)
			padX += parent.getPaddedX();
		
		return padX;
	}
	
	/**
	 * Get the y position of this component plus the top padding
	 * @return The "padded" y of this GUIComponent
	 */
	public int getPaddedY() {
		int padY = y + pTop;
		
		if (parent != null)
			padY += parent.getPaddedY();
		
		return padY;
	}
	
	/**
	 * Set the padding (in pixels) around this element. For layout purposes, padding counts as being part of the
	 * element but the element is rendered inside of the padded area.
	 * @param top Padding on the top
	 * @param bottom Padding on the bottom
	 * @param left Padding on the left
	 * @param right Padding on the right
	 */
	public void setPadding(int top, int bottom, int left, int right) {
		updated = top != pTop || bottom != pBottom || left != pLeft || right != pRight;
		pTop = top;
		pBottom = bottom;
		pLeft = left;
		pRight = right;
	}
	
	/**
	 * Set padding on all sides of the element
	 * @param p The padding
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPadding(int p) {
		setPadding(p, p, p, p);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingY(int top, int bottom) {
		setPadding(top, bottom, pLeft, pRight);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingX(int left, int right) {
		setPadding(pTop, pBottom, left, right);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingTop(int top) {
		setPadding(top, pBottom, pLeft, pRight);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingBottom(int bottom) {
		setPadding(pTop, bottom, pLeft, pRight);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingLeft(int left) {
		setPadding(pTop, pBottom, left, pRight);
	}
	
	/**
	 * @see GUIComponent#setPadding(int, int, int, int)
	 */
	public void setPaddingRight(int right) {
		setPadding(pTop, pBottom, pLeft, right);
	}
	
	/**
	 * Get the type of this GUIComponent. One of TYPE_IMAGE, TYPE_FRAME, or TYPE_LABEL. This streamlines
	 * the rendering process.
	 * @return The type of this GUIComponent
	 * @see GUITypeIdentifier
	 */
	public GUITypeIdentifier getType() {
		return type;
	}
	
	/**
	 * Get the parent of this GUIComponent.
	 * @return The parent. If there is no parent, this returns null.
	 */
	public GUIFrame getParent() {
		return parent;
	}
	
	/**
	 * Set the parent frame to this GUIComponent. As of this version, this is unpredictable if
	 * called more than once because the component is never removed from the parent's list of 
	 * children. This will be changed in future version.
	 * @param parent The parent frame
	 * @param dict The settings to use for this parent's layout manager. Can be changed later through {@link GUIComponent#setLayoutSettings(GUILayoutSettings)}
	 */
	public void setParent(GUIFrame parent, GUILayoutSettings dict) {
		parent.add(this, dict);
		this.parent = parent;
	}
	
	/**
	 * Get the layout settings being used by this component's parent to decide placement.
	 * @return
	 */
	public GUILayoutSettings getLayoutSettings() {
		return settings;
	}
	
	/**
	 * Set the settings that will be used by this component's parent layout manager to decide
	 * placement. The layout manager will throw an error at runtime if the type of the settings
	 * does not match it's type.
	 * @param settings The new layout settings
	 */
	public void setLayoutSettings(GUILayoutSettings settings) {
		this.settings = settings;
	}
	
	/**
	 * Check if a GUIComponent shares the UID of this GUIComponent
	 * @param other The other GUIComponent
	 * @return Whether or not the two are equal
	 */
	public boolean equals(GUIComponent other) {
		return this.uid == other.getUID();
	}
	
	/**
	 * Get a unique ID that belongs to this specific GUIComponent. Used to distinguish components
	 * from each other without extraneous checks.
	 * @return A unique ID, distinguishable among GUIComponents.
	 */
	public long getUID() {
		return uid;
	}
	
	/**
	 * Check if the dimensions of this GUIComponent have been updated since the last
	 * call to this function.
	 * @return Whether or not the Dimensions have changed
	 */
	public boolean hasBeenUpdated() {
		// DimensionMeasurements keep update statuses separately
		return this.popUpdate() || width.popUpdate() || height.popUpdate();
	}
	
	/**
	 * Gets the local update status and resets it to false after checking.
	 * @return The local update status (anything that isn't encompassed within a DimensionMeasurement)
	 */
	private boolean popUpdate() {
		boolean old = updated;
		updated = false;
		return old;
	}
	
	/**
	 * Get the max bounds of this GUIComponent, i.e. the bounds that this component can
	 * reside within without overflowing. This is generally equivalent to the internal bounding
	 * area of the parent (that is to say, excluding padded edges) unless the parent is null
	 * in which case, it is equal to the total window size.
	 * @return Max bounds of this GUIComponent
	 */
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
	
	/**
	 * Get the amount of space this component resides within. This includes padding.
	 * @return The bounds (including padding) of this GUIComponent
	 */
	public Rectangle getBounds() {
		int width = getPaddedWidth();
		int height = getPaddedHeight();
		int x = getX();
		int y = getY();
		return new Rectangle(x, y, width, height);
	}
	
	/**
	 * Decide the bounds of this GUIComponent and the members thereof (if it is a frame).
	 */
	public void update() {};
	
	/**
	 * Based on the previously calculated bounds, decide exactly where this component (if it is a frame)
	 * will place all of its members.
	 */
	public void place() {};
}
