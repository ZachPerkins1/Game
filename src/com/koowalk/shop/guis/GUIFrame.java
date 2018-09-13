package com.koowalk.shop.guis;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import com.koowalk.shop.guis.GUITypeIdentifier;
import com.koowalk.shop.util.Dim;

public class GUIFrame extends GUIComponent {
	public ArrayList<GUIComponent> children;
	private Color color;
	
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
	}
	
	public void add(GUIComponent component, GUILayoutSettings settings) {
		children.add(component);
		layoutManager.add(component, settings);
	}
	
	public ArrayList<GUIComponent> getChildren() {
		return children;
	}
	
	public void setLayoutManager(GUILayout manager) {
		layoutManager = manager;
	}
	
	public void update() {
		for (GUIComponent child : children) {
			child.update();
		}
		
		layoutManager.update();
		this.getWidthMeasurement().setAuto(layoutManager.getBoundingWidth());
		this.getHeightMeasurement().setAuto(layoutManager.getBoundingHeight());
	}
	
	public void place() {
		for (GUIComponent child : children) {
			child.place();
		}
		
		layoutManager.place();
	}
	
	public Color getColor() {
		return color;
	}
}
