package com.koowalk.shop.guis;

import java.awt.Dimension;

import com.koowalk.shop.util.Dim;

public class GUILayoutAbsolute extends GUILayout {
	private int maxWidth = 0;
	private int maxHeight = 0;
	
	private boolean dimensionUpdateRequired = true;
	
	@Override
	public void update() {
		dimensionUpdateRequired = true;
	}

	@Override
	public int getBoundingWidth() {
		updateDimensionsIfRequired();
		return maxWidth;
	}

	@Override
	public int getBoundingHeight() {
		updateDimensionsIfRequired();
		return maxHeight;
	}

	@Override
	public void place() {
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsAbsolute settings = (GUILayoutSettingsAbsolute) component.getLayoutSettings();
			component.x = settings.x;
			component.y = settings.y;
		}
	}
	
	private void updateDimensionsIfRequired() {
		if (dimensionUpdateRequired) {
			for (GUIComponent component : getComponents()) {
				GUILayoutSettingsAbsolute settings = (GUILayoutSettingsAbsolute) component.getLayoutSettings();
				int height = settings.y + component.getPaddedHeight();
				int width = settings.x + component.getPaddedWidth();
					
				if (height > maxHeight) maxHeight = height;
				if (width > maxWidth) maxWidth = width;
			}
		
			dimensionUpdateRequired = false;
		}
	}
}
