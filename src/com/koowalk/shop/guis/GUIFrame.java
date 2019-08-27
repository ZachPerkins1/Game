package com.koowalk.shop.guis;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;

import com.koowalk.shop.guis.GUITypeIdentifier;
import com.koowalk.shop.util.Dim;

public class GUIFrame extends GUIComponent {
	private static class ZIndexSorter implements Comparator<GUIComponent> {
		@Override
		public int compare(GUIComponent arg0, GUIComponent arg1) {
			return arg0.getZIndex() - arg1.getZIndex();
		}	
	}
	
	public LinkedList<GUIComponent> children;
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
		children = new LinkedList<GUIComponent>();
		manager.setParentBounds(getWidthMeasurement(), getHeightMeasurement());
		layoutManager = manager;
	}
	
	public void add(GUIComponent component, GUILayoutSettings settings) {
		children.add(component);
		layoutManager.add(component, settings);
		children.sort(new ZIndexSorter());
	}
	
	public void reorderChild(GUIComponent child) {
		children.remove(child);
		children.add(child);
		children.sort(new ZIndexSorter());
	}
	
	public void sendToTop(GUIComponent child) {
		children.remove(child);
		child.zindex = children.getLast().zindex + 1;
		children.add(child);
	}
	
	public void sendToBottom(GUIComponent child) {
		children.remove(child);
		child.zindex = children.getFirst().zindex - 1;
		children.addFirst(child);
	}
	
	@Override
	public GUIComponent processClick(int x, int y) {
		GUIComponent target = null;
		
		for (GUIComponent c : children) {
			if (c.intersectsClick(x, y)) {
				target = c.processClick(x, y);
			}
		}
		
		return target;
	}
	
	public List<GUIComponent> getChildren() {
		return children;
	}
	
	public void setLayoutManager(GUILayout manager) {
		layoutManager = manager;
	}
	
	public GUILayout getLayoutManager() {
		return layoutManager;
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
