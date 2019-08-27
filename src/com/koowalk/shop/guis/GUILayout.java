package com.koowalk.shop.guis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import com.koowalk.shop.guis.dimension.DimensionMeasurement;
import com.koowalk.shop.util.Dim;

public abstract class GUILayout {
	private ArrayList<GUIComponent> components;
	
	private DimensionMeasurement pWidth;
	private DimensionMeasurement pHeight;
	
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
	
	public int getBoundingByDim(Dim d) {
		if (d == Dim.X) {
			return getBoundingWidth();
		} else if (d == Dim.Y) {
			return getBoundingHeight();
		}
		
		return 0;
	}
	
	public DimensionMeasurement getParentWidthMeasurement() {
		return pWidth;
	}
	
	public DimensionMeasurement getParentHeightMeasurement() {
		return pHeight;
	}
	
	public DimensionMeasurement getParentMeasurementByDim(Dim d) {
		if (d == Dim.X) {
			return getParentWidthMeasurement();
		} else if (d == Dim.Y) {
			return getParentHeightMeasurement();
		}
		
		return null;
	}
	
	public void setParentBounds(DimensionMeasurement w, DimensionMeasurement h) {
		this.pWidth = w;
		this.pHeight = h;
	}
	
	public abstract int getBoundingWidth();
	public abstract int getBoundingHeight();
	public abstract void update(); // Update the widths and heights of all units contained within this component
	public abstract void place(); // Decide coord positions based on previously established dimension
}
