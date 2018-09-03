package com.koowalk.shop.guis;

public class GUILayoutAbsolute extends GUILayout {
	private int maxWidth = 0;
	private int maxHeight = 0;
	
	@Override
	public void update() {
		for (GUILayoutComponent component : components) {
			GUILayoutSettingsAbsolute settings = (GUILayoutSettingsAbsolute) component.getLayoutSettings();
			component.x = settings.x;
			component.y = settings.y;
			
			int height = component.y + component.getPaddedHeight();
			int width = component.x + component.getPaddedWidth();
				
			if (height > maxHeight) maxHeight = height;
			if (width > maxWidth) maxWidth = width;
		}
	}

	@Override
	public int getBoundingWidth() {
		return maxWidth;
	}

	@Override
	public int getBoundingHeight() {
		return maxHeight;
	}
}
