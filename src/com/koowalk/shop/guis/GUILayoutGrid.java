package com.koowalk.shop.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.koowalk.shop.guis.GUILayoutSettingsGrid.Sticky;
import com.koowalk.shop.guis.dimension.Dimension;
import com.koowalk.shop.guis.dimension.DimensionMeasurement;
import com.koowalk.shop.guis.dimension.DimensionMeasurement.Mode;
import com.koowalk.shop.util.Container;
import com.koowalk.shop.util.Dim;
import com.koowalk.shop.util.Logger;

public class GUILayoutGrid extends GUILayout {
	public static class TableMap extends HashMap<Integer, Container<Dimension>> {
		public Dimension get(int i) {
			try {
				return super.get(i).get();
			} catch (Exception e) {
				return null;
			}
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
	
	private Container<Dimension> width;
	private Container<Dimension> height;
		
	public GUILayoutGrid() {
		rowDefaults = new TableMap();
		columnDefaults = new TableMap();
		
		width = new Container<Dimension>();
		height = new Container<Dimension>();
	}
 
	@Override
	public int getBoundingWidth() {
		return totalSize(columnSizes);
	}

	@Override
	public int getBoundingHeight() {
		return totalSize(rowSizes);
	}
	
	private Container<Dimension> getAvailableWidth() {
		return width;
	}
	
	private Container<Dimension> getAvailableHeight() {
		return height;
	}
	
	private Container<Dimension> getAvailableByDim(Dim d) {
		return Dim.switchDimXY(d, getAvailableWidth(), getAvailableHeight());
	}
	
	public void setColumnSize(int c, DimensionMeasurement d) {
		d.setParent(width);
		columnDefaults.put(c, d);
	}
	
	public void setRowSize(int r, DimensionMeasurement d) {
		d.setParent(height);
		rowDefaults.put(r, d);
	}
	
	// Update related functions //

	@Override
	public void update() {
		columnSizes = new TableMap();
		rowSizes = new TableMap();
				
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsGrid settings = (GUILayoutSettingsGrid) component.getLayoutSettings();
			
			DimensionMeasurement width = component.getDimensionMeasurementByDim(Dim.X);
			DimensionMeasurement height = component.getDimensionMeasurementByDim(Dim.Y);
			
			if (width.getMode() != Mode.RELATIVE)
				setMaxInDimension(columnSizes, settings.column, 
						width.add(settings.getAdditionalWidth() + component.getPaddingX()));
			if (height.getMode() != Mode.RELATIVE)
				setMaxInDimension(rowSizes, settings.row, 
						height.add(settings.getAdditionalHeight() + component.getPaddingY()));
			
			width.setParent(columnSizes.getContainer(settings.column));
			height.setParent(rowSizes.getContainer(settings.row));
		}
		
		applyDefaults(rowSizes, columnSizes);
		
		totalAvailableSize(Dim.X, columnSizes);
		totalAvailableSize(Dim.Y, rowSizes);
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
	
	//TODO: Make this not bad
	private void totalAvailableSize(Dim d, TableMap table) {
		Container<Dimension> dim = getAvailableByDim(d);
		Dimension totalSize = getParentMeasurementByDim(d);
		
		for (int i : table.keySet()) {
			Dimension j = table.get(i);
			if (j instanceof DimensionMeasurement && 
					((DimensionMeasurement)j).getMode() == Mode.RELATIVE) {
				continue;
			} else {
				totalSize = Dimension.subtract(totalSize, j);
			}
		}
			
		dim.set(totalSize);
	}
	
	private int totalSize(TableMap table) {
		int totalSize = 0;
		
		for (int i : table.keySet())
			totalSize += table.getValueOrDefault(i, 0);
		
		return totalSize;
	}
	
	// All functions for placement //
	
	@Override
	public void place() {
		placeComponents();
	}
		
	private void placeComponents() {
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
}
