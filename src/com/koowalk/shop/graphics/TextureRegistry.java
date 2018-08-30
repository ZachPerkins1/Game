package com.koowalk.shop.graphics;

import java.util.ArrayList;

public class TextureRegistry {
	private static ArrayList<LoadableTexture> registry = new ArrayList<LoadableTexture>();
	
	public static int add(LoadableTexture tex) {
		registry.add(tex);
		return registry.indexOf(tex);
	}
	
	public static void load() {
		for (LoadableTexture l : registry)
			l.load();
	}
	
	public static LoadableTexture get(int id) {
		return registry.get(id);
	}
}
