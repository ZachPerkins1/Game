package com.koowalk.shop.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class GLImage implements LoadableTexture {
	protected int width;
	protected int height;
	protected int glId;
	
	public GLImage(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public GLImage() {
		this(0, 0);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	// Call if the texture is to be used by itself
	public abstract void load();
	
	public void use(int binding, GLProgram program, String handle) {
		glActiveTexture(GL_TEXTURE0 + binding);
		glBindTexture(GL_TEXTURE_2D, glId);
		program.setUniform(handle, binding);
	}
	
	public void use(GLProgram program, String handle) {
		use(0, program, handle);
	}
}
