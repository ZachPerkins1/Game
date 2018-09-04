package com.koowalk.shop.guis;

import java.util.HashMap;

public class GUILayoutSettingsGrid extends GUILayoutSettings {
	int row;
	int column;
	int marginLeft;
	int marginRight;
	int marginTop;
	int marginBottom;
	
	public GUILayoutSettingsGrid(HashMap<String, Object> dict) {
		super(dict);
	}

	@Override
	public void load(HashMap<String, Object> dict) {
		row = (int) dict.getOrDefault("row", 0);
		column = (int) dict.getOrDefault("column", 0);
		marginLeft = (int) dict.getOrDefault("margin-left", 0);
		marginRight = (int) dict.getOrDefault("margin-right", 0);
		marginTop = (int) dict.getOrDefault("margin-top", 0);
		marginBottom = (int) dict.getOrDefault("margin-bottom", 0);
	}
	
	public int getAdditionalWidth() {
		return marginLeft + marginRight;
	}
	
	public int getAdditionalHeight() {
		return marginTop + marginBottom;
	}
}
