package com.koowalk.shop.guis;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map.Entry;

import com.koowalk.shop.guis.GUILayoutSettingsGrid.Sticky;

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
		
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			int width = settings.getAdditionalWidth() + component.getPaddedWidth();
			int height = settings.getAdditionalHeight() + component.getPaddedHeight();
			
			if (columnSizes.getOrDefault(settings.column, 0) < width)
				columnSizes.put(settings.column, width);
			
			if (rowSizes.getOrDefault(settings.row, 0) < height)
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
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			int offset = 0;
			for (int i = settings.column - 1; i >= 0; i--) {
				offset += columnSizes.getOrDefault(i, 0);
			}
			component.x = decideCoord(offset, columnSizes.get(settings.column), component.getPaddedWidth(), 
					settings.marginLeft, settings.marginRight, settings.floatX);
			// component.x += settings.marginLeft;
			
			offset = 0;
			for (int i = settings.row - 1; i >= 0; i--) {
				offset += rowSizes.getOrDefault(i, 0);
			}
			component.y = decideCoord(offset, rowSizes.get(settings.row), component.getPaddedHeight(), 
					settings.marginTop, settings.marginBottom, settings.floatY);
		}
	}
	
	private int decideCoord(int offset, int gridSize, int componentSize, int marginPositive, int marginNegative, 
			GUILayoutSettingsGrid.Sticky sticky) {
		if (sticky == Sticky.POSITIVE) {
			return offset + marginPositive;
		} else if (sticky == Sticky.NEGATIVE) {
			return offset + gridSize - componentSize - marginNegative;
		} else {
			return ((offset + (gridSize / 2))) - ((componentSize + marginPositive + marginNegative) / 2) + marginPositive;
		}
	}

	@Override
	public Dimension getComponentAlottedDimensions(GUIComponent component) {
		// TODO Auto-generated method stub
		return null;
	}
}
