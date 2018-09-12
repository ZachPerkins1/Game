package com.koowalk.shop.guis;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import com.koowalk.shop.guis.GUITypeIdentifier;
import com.koowalk.shop.util.Dim;

public class GUIFrame extends GUIComponent {
	public ArrayList<GUIComponent> children;
	private Color color;
	private DimensionMeasurement width;
	private DimensionMeasurement height;
	
	private GUILayout layoutManager;
	
	public GUIFrame() {
		this(new GUILayoutAbsolute());
	}
	
	public GUIFrame(Color color) {
		this(new GUILayoutAbsolute(), color);
	}
	
	public GUIFrame(GUILayout layout) {
		this(layout, new Color(0,0,0,0));
	}
	
	public GUIFrame(GUILayout manager, Color color) {
		super(GUITypeIdentifier.TYPE_FRAME);
		this.color = color;
		children = new ArrayList<GUIComponent>();
		layoutManager = manager;
		width = new DimensionMeasurement();
		height = new DimensionMeasurement();
	}
	
	public void add(GUIComponent component, GUILayoutSettings settings) {
		children.add(component);
		layoutManager.add(component, settings);
	}
	
	public ArrayList<GUIComponent> getChildren() {
		return children;
	}
	
	public int getChildAlottedDimension(GUIComponent child, Dim d) {
		if (getDimensionMeasurementByDim(d).getMode() == DimensionMeasurement.Mode.AUTO) {
			
		}
		
		layoutManager.getComponentAlottedDimension(child, d);
		return 0;
	}
	
	public void setLayoutManager(GUILayout manager) {
		layoutManager = manager;
	}
	
	public void update() {
		for (GUIComponent child : children) {
			child.update();
		}
		
		layoutManager.update();
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getWidth() {
		return getDimension(Dim.X);
	}
	
	public int getHeight() {
		return getDimension(Dim.Y);
	}
	
	private int getDimension(Dim d) {
		DimensionMeasurement m = getDimensionMeasurementByDim(d);
		
		if (m.getMode() == DimensionMeasurement.Mode.AUTO) {
			return layoutManager.getBoundingByDim(d);
		} else if (m.getMode() == DimensionMeasurement.Mode.RELATIVE) {
			return getParent().getChildAlottedDimension(this, d);
		} else {
			return m.get();
		}
	}
	
	public DimensionMeasurement getWidthMeasurement() {
		return width;
	}
	
	public DimensionMeasurement getHeightMeasurement() {
		return height;
	}
	
	public DimensionMeasurement getDimensionMeasurementByDim(Dim d) {
		if (d == Dim.X) {
			return getWidthMeasurement();
		} else if (d == Dim.Y) {
			return getHeightMeasurement();
		}
		
		return null;
	}
}
