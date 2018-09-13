package com.koowalk.shop.guis;

import com.koowalk.shop.graphics.GLTexture;
import com.koowalk.shop.graphics.TextureRegistry;

public class GUIImage extends GUIComponent {
	private GLTexture image;
	
	public GUIImage(int id) {
		super(GUITypeIdentifier.TYPE_IMAGE);
		image = (GLTexture) TextureRegistry.get(id);
	}
	
	public GLTexture getImage() {
		return image;
	}
	
	public void update() {
		this.getWidthMeasurement().setAuto(image.getWidth());
		this.getHeightMeasurement().setAuto(image.getHeight());
	}
}