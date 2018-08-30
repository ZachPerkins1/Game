package com.koowalk.shop.guis;

import java.util.HashMap;

public abstract class GUILayoutSettings {
	public GUILayoutSettings(HashMap<String, Object> dict) {
		load(dict);
	}
	
	public abstract void load(HashMap<String, Object> dict);
}
