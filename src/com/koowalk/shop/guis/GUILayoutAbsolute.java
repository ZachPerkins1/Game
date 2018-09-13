package com.koowalk.shop.guis;

import java.awt.Dimension;

import com.koowalk.shop.util.Dim;

public class GUILayoutAbsolute extends GUILayout {
	private int maxWidth = 0;
	private int maxHeight = 0;
	
	@Override
	public void update() {
		
	}

	@Override
	public int getBoundingWidth() {
		System.out.println(maxWidth);
		return maxWidth;
	}

	@Override
	public int getBoundingHeight() {
		return maxHeight;
	}

	@Override
	public void place() {
		for (GUIComponent component : getComponents()) {
			GUILayoutSettingsAbsolute settings = (GUILayoutSettingsAbsolute) component.getLayoutSettings();
			component.x = settings.x;
			component.y = settings.y;
			
			int height = component.y + component.getPaddedHeight();
			int width = component.x + component.getPaddedWidth();
				
			if (height > maxHeight) maxHeight = height;
			if (width > maxWidth) maxWidth = width;
		}
	}
}
