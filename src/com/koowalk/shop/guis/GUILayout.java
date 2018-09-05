package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GUILayout {
	private ArrayList<GUILayoutComponent> components;
	
	public GUILayout() {
		components = new ArrayList<GUILayoutComponent>();
	}
	
	public void add(GUILayoutComponent component, GUILayoutSettings settings) {
		component.setLayoutSettings(settings);
		components.add(component);
	}
	
	public ArrayList<GUILayoutComponent> getComponents() {
		return components;
	}
	
	public abstract int getBoundingWidth();
	public abstract int getBoundingHeight();
	public abstract void update();
}
