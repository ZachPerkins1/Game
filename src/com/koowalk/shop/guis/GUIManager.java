package com.koowalk.shop.guis;

import java.awt.Color;

import com.koowalk.shop.graphics.GUIRenderEngine;

public class GUIManager {
	private static GUIManager instance = null;
	private GUIRenderEngine renderEngine;
	
	private int screenWidth;
	private int screenHeight;
	
	private GUIFrame master;
	
	public GUIManager() {
		master = new GUIFrame(new GUILayoutGrid(), Color.BLUE);
		renderEngine = new GUIRenderEngine();
	}
	
	public void updateScreenSize(int minX, int maxX, int minY, int maxY) {
		
	}
	
	public GUIFrame getMaster() {
		return master;
	}
	
	public void update() {
		master.update();
		master.place();
	}
	
	private void render(GUIFrame frame) {
		renderEngine.drawComponent(frame);
		for (GUIComponent child : frame.getChildren()) {
			renderEngine.drawComponent(child);
			if (child.getType() == GUITypeIdentifier.TYPE_FRAME)
				render((GUIFrame)child);
		}
	}
	
	public void draw() {
		render(master);
	}
	
	public void issueLayoutUpdates() {
		master.update();
		master.place();
	}
	
	public static GUIManager getInstance() {
		if (instance == null) 
			instance = new GUIManager();
		
		return instance;
	}
}
