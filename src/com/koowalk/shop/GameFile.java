package com.koowalk.shop;

import java.io.File;

import com.koowalk.shop.world.MapFile;

public class GameFile {
	public static final String SAVE_FOLDER = "saves";
	
	String filename;
	MapFile map;
	
	public GameFile(String gamefile) {
		File file = new File(SAVE_FOLDER + "/" + gamefile);
		
		if (file.exists() && file.isDirectory()) {
			load();
		} else {
			file.mkdirs();
		}
		
		map = new MapFile(file);
	}
	
	private void load() {
		// map.load();
	}
	
	public MapFile getMap() {
		return map;
	}
}
