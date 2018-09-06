package com.koowalk.shop.guis;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import com.koowalk.shop.guis.GUITypeIdentifier;

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
	
	public Dimension getChildAlottedDimensions(GUIComponent child) {
		return null;
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
		if (width.getMode() == DimensionMeasurement.Mode.AUTO) {
			return layoutManager.getBoundingWidth();
		} else if (width.getMode() == DimensionMeasurement.Mode.RELATIVE) {
			if (getParent().getWidthMeasurement().getMode() == DimensionMeasurement.Mode.AUTO) {
				return 0;
			} else {
				return width.get(getParent().getWidth());
			}
		} else {
			return width.get();
		}
	}
	
	public int getHeight() {
		return layoutManager.getBoundingHeight();
	}
	
	public DimensionMeasurement getWidthMeasurement() {
		return width;
	}
	
	public DimensionMeasurement getHeightMeasurement() {
		return height;
	}
}
