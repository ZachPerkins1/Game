package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GUILayout {
	public ArrayList<GUILayoutComponent> components;
	
	public GUILayout() {
		components = new ArrayList<GUILayoutComponent>();
	}
	
	public void add(GUILayoutComponent component, HashMap<String, Object> dict) {
		component.setLayoutSettings(new GUILayoutSettingsAbsolute(dict));
		components.add(component);
	}
	
	public abstract void update();
}
