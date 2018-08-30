package com.koowalk.shop.guis;

import java.util.HashMap;

public class GUILayoutSettingsAbsolute extends GUILayoutSettings {
	public int x;
	public int y;
	
	public GUILayoutSettingsAbsolute(HashMap<String, Object> dict) {
		super(dict);
	}
	
	public void load(HashMap<String, Object> dict) {
		x = (Integer) dict.getOrDefault("x", 0);
		y = (Integer) dict.getOrDefault("y", 0);
	}
}
