package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GUILayout {
	private ArrayList<GUILayoutComponent> components;
	
	public GUILayout() {
		components = new ArrayList<GUILayoutComponent>();
	}
	
	public void add(GUILayoutComponent component, HashMap<String, Object> dict) {
		component.setLayoutSettings(new GUILayoutSettingsAbsolute(dict));
		components.add(component);
	}
	
	public ArrayList<GUILayoutComponent> getComponents() {
		return components;
	}
	
	public abstract int getBoundingWidth();
	public abstract int getBoundingHeight();
	public abstract void update();
}
