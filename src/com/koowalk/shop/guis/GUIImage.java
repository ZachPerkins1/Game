package com.koowalk.shop.guis;

import com.koowalk.shop.graphics.GLTexture;
import com.koowalk.shop.graphics.TextureRegistry;

public class GUIImage extends GUIComponent {
	private GLTexture image;
	
	public GUIImage(int id) {
		super(GUITypeIdentifier.TYPE_IMAGE);
		image = (GLTexture) TextureRegistry.get(id);
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public GLTexture getImage() {
		return image;
	}
}