package com.koowalk.shop.graphics;

public class CompoundTexture {
	private String[] textures;
	
	private int index;
	private int count;
	
	private boolean loaded = false;
	
	public CompoundTexture(int size) {
		textures = new String[size];
		count = size;
	}
	
	public CompoundTexture(String... textures) {
		this(textures.length);
		for (int i = 0; i < textures.length; i++) {
			set(i, textures[i]);
		}
	}
	
	public void set(int i, String texture) {
		if (loaded) { 
			throw new IllegalStateException("Cannot update textures after loading into GLTextureArray");
		}
		
		textures[i] = texture;
	}
	
	public int getDefault() {
		return index;
	}
	
	public int getSub(int s) {
		return index + s;
	}
	
	public void load(GLTextureArray array) {
		if (loaded) {
			throw new IllegalStateException("Compound texture is already loaded");
		}
		
		for (int i = 0; i < count; i++) {
			if (textures[i] == null) {
				throw new NullPointerException("All textures in compound texture must have file references");
			} else {
				array.add(textures[i]);
			}
		}
		
		textures = null;
	}
}
