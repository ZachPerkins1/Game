package com.koowalk.shop.guis;

import com.koowalk.shop.graphics.GUIRenderEngine;

public class GUIManager {
	private static GUIManager instance = null;
	private GUIRenderEngine renderEngine;
	
	private int screenWidth;
	private int screenHeight;
	
	private GUIFrame master;
	
	public GUIManager() {
		master = new GUIFrame();
		renderEngine = new GUIRenderEngine();
	}
	
	public void updateScreenSize(int minX, int maxX, int minY, int maxY) {
		
	}
	
	public GUIFrame getMaster() {
		return master;
	}
	
	private void render(GUIFrame frame) {
		for (GUIComponent child : frame.getChildren()) {
			if (child.getType() == GUITypeIdentifier.TYPE_FRAME) {
				render((GUIFrame)child);
			} else {
				renderEngine.drawComponent(child);
			}
		}
	}
	
	public void draw() {
		render(master);
	}
	
	public void issueLayoutUpdates() {
		master.update();
	}
	
	public static GUIManager getInstance() {
		if (instance == null) 
			instance = new GUIManager();
		
		return instance;
	}
}
