package com.koowalk.shop.guis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import com.koowalk.shop.util.Dim;

public abstract class GUILayout {
	private ArrayList<GUIComponent> components;
	
	public GUILayout() {
		components = new ArrayList<GUIComponent>();
	}
	
	public void add(GUIComponent component, GUILayoutSettings settings) {
		component.setLayoutSettings(settings);
		components.add(component);
	}
	
	public ArrayList<GUIComponent> getComponents() {
		return components;
	}
	
	public abstract int getBoundingWidth();
	public abstract int getBoundingHeight();
	
	public int getBoundingByDim(Dim d) {
		if (d == Dim.X) {
			return getBoundingWidth();
		} else if (d == Dim.Y) {
			return getBoundingHeight();
		}
		
		return 0;
	}
	
	public abstract void update();
	
	public int getComponentAlottedDimension(GUIComponent component, Dim d) {
		if (d == Dim.X) {
			return getComponentAlottedWidth(component);
		} else if (d == Dim.Y) {
			return getComponentAlottedHeight(component);
		}
		
		return 0;
	}
	
	public abstract int getComponentAlottedWidth(GUIComponent component);
	public abstract int getComponentAlottedHeight(GUIComponent component);
}
