package com.koowalk.shop.guis;

public class GUILayoutAbsolute extends GUILayout {
	@Override
	public void update() {
		for (GUILayoutComponent component : components) {
			GUILayoutSettingsAbsolute settings = (GUILayoutSettingsAbsolute) component.getLayoutSettings();
			component.x = settings.x;
			component.y = settings.y;
		}
	}
}
