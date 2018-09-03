package com.koowalk.shop.guis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.koowalk.shop.guis.GUITypeIdentifier;

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
	
	public void add(GUIComponent component, HashMap<String, Object> dict) {
		children.add(component);
		layoutManager.add(component, dict);
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
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getWidth() {
		return layoutManager.getBoundingWidth();
	}
	
	public int getHeight() {
		return layoutManager.getBoundingHeight();
	}
}
