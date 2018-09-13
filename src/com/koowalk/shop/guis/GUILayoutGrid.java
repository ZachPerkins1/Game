package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.koowalk.shop.guis.GUILayoutSettingsGrid.Sticky;
import com.koowalk.shop.util.Container;
import com.koowalk.shop.util.Dim;

public class GUILayoutGrid extends GUILayout {
	public static class TableMap extends HashMap<Integer, Container<Dimension>> {
		public Dimension get(int i) {
			return super.get(i).get();
		}
		
		public int getValue(int i) {
			return super.get(i).get().get();
		}
		
		public int getValueOrDefault(int i, int d) {
			try {
				return getValue(i);
			} catch (Exception e) {
				return d;
			}
		}
		
		public Container<Dimension> getContainer(int i) {
			return super.get(i);
		}
		
		public void put(int i, Dimension d) {
			Container<Dimension> dim = super.get(i);
			if (dim == null) {
				dim = new Container<Dimension>();
				super.put(i, dim);
			}
			
			dim.set(d);
		}
	}
	
	private TableMap rowDefaults;
	private TableMap columnDefaults;
	
	private TableMap columnSizes;
	private TableMap rowSizes;
	
	private int totalWidth;
	private int totalHeight;
	
	public GUILayoutGrid() {
		rowDefaults = new TableMap();
		columnDefaults = new TableMap();
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
		columnSizes = new TableMap();
		rowSizes = new TableMap();
				
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			
			DimensionMeasurement width = component.getDimensionMeasurementByDim(Dim.X);
			DimensionMeasurement height = component.getDimensionMeasurementByDim(Dim.Y);
			
			setMaxInDimension(columnSizes, settings.column, 
					width.add(settings.getAdditionalWidth() + component.getPaddedWidth()));
			setMaxInDimension(rowSizes, settings.row, 
					height.add(settings.getAdditionalHeight() + component.getPaddedHeight()));
			
			width.setParent(columnSizes.getContainer(settings.column));
			height.setParent(rowSizes.getContainer(settings.row));
		}
		
		applyDefaults(rowSizes, columnSizes);
	}
	
	@Override
	public void place() {
		placeComponents();
		totalSizes();
	}
	
	private void totalSizes() {
		totalWidth = 0;
		totalHeight = 0;
		
		for (int i : columnSizes.keySet())
			totalWidth += columnSizes.get(i).get();
		for (int i : rowSizes.keySet()) 
			totalHeight += rowSizes.get(i).get();
	}
		
	private void placeComponents() {
		totalWidth = 0;
		totalHeight = 0;
		
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			int offset = 0;
			for (int i = settings.column - 1; i >= 0; i--) {
				offset += columnSizes.getValueOrDefault(i, 0);
			}
			component.x = decideCoord(offset, columnSizes.getValue(settings.column), component.getPaddedWidth(), 
					settings.marginLeft, settings.marginRight, settings.floatX);
			// component.x += settings.marginLeft;
			
			offset = 0;
			for (int i = settings.row - 1; i >= 0; i--) {
				offset += rowSizes.getValueOrDefault(i, 0);
			}
			component.y = decideCoord(offset, rowSizes.getValue(settings.row), component.getPaddedHeight(), 
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
	
	private void applyDefaults(TableMap rowSizes, TableMap columnSizes) {
		for (Entry<Integer, Container<Dimension>> data : columnDefaults.entrySet()) {
			columnSizes.put(data.getKey(), data.getValue().get());
		}
		
		for (Entry<Integer, Container<Dimension>> data : rowDefaults.entrySet()) {
			rowSizes.put(data.getKey(), data.getValue().get());
		}
	}
	
	private void setMaxInDimension(TableMap map, int i, Dimension d) {
		if (map.get(i) == null) {
			map.put(i, d);
		} else {
			map.put(i, Dimension.max(d, map.get(i)));
		}
	}
}
