package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;

import com.koowalk.shop.guis.GUITypeIdentifier;

public class GUIFrame extends GUIComponent {
	public ArrayList<GUIComponent> children;
	
	private GUILayout layoutManager;
	
	public GUIFrame() {
		this(new GUILayoutAbsolute());
	}
	
	public GUIFrame(GUILayout manager) {
		super(GUITypeIdentifier.TYPE_FRAME);
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
		layoutManager.update();
		for (GUIComponent child : children) {
			child.update();
		}
	}
}
