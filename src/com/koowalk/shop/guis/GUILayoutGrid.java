package com.koowalk.shop.guis;

import java.util.HashMap;
import java.util.Map.Entry;

public class GUILayoutGrid extends GUILayout {
	private HashMap<Integer, Integer> rowDefaults;
	private HashMap<Integer, Integer> columnDefaults;
	
	private int totalWidth;
	private int totalHeight;
	
	public GUILayoutGrid() {
		rowDefaults = new HashMap<Integer, Integer>();
		columnDefaults = new HashMap<Integer, Integer>();
	}
 
	@Override
	public int getBoundingWidth() {
		return totalWidth;
	}

	@Override
	public int getBoundingHeight() {
		return totalHeight;
	}

	@Override
	public void update() {
		HashMap<Integer, Integer> columnSizes = new HashMap<Integer, Integer>(columnDefaults);
		HashMap<Integer, Integer> rowSizes = new HashMap<Integer, Integer>(rowDefaults);
		
		for (GUILayoutComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			int width = settings.getAdditionalWidth() + component.getPaddedWidth();
			int height = settings.getAdditionalHeight() + component.getPaddedHeight();
			
			if (columnSizes.get(settings.column) < width)
				columnSizes.put(settings.column, width);
			
			if (rowSizes.get(settings.row) < height)
				rowSizes.put(settings.row, height);	
		}
		
		for (Entry<Integer, Integer> data : rowDefaults.entrySet()) {
			rowSizes.put(data.getKey(), data.getValue());
		}
		
		for (Entry<Integer, Integer> data : columnDefaults.entrySet()) {
			columnSizes.put(data.getKey(), data.getValue());
		}
		
		placeComponents(rowSizes, columnSizes);
		totalSizes(rowSizes, columnSizes);
	}
	
	private void totalSizes(HashMap<Integer, Integer> rowSizes, HashMap<Integer, Integer> columnSizes) {
		totalWidth = 0;
		totalHeight = 0;
		
		for (int i : columnSizes.values())
			totalWidth += i;
		for (int i : rowSizes.values()) 
			totalHeight += i;
	}
	
	private void placeComponents(HashMap<Integer, Integer> rowSizes, HashMap<Integer, Integer> columnSizes) {
		for (GUILayoutComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			for (int i = settings.column - 1; i >= 0; i--) {
				component.x += columnSizes.getOrDefault(i, 0) + settings.marginLeft;
			}
			
			for (int i = settings.row - 1; i >= 0; i--) {
				component.y += rowSizes.getOrDefault(i, 0) + settings.marginTop;
			}
		}
	}

}
