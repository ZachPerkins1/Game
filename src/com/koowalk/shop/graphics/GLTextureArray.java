package com.koowalk.shop.graphics;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL45.*;

public class GLTextureArray implements LoadableTexture {
	private int glId;
	private int[] raw;
	private int counter;
	
	private int width;
	private int height;
	
	public GLTextureArray(int w, int h) {
		width = w;
		height = h;
		
		raw = new int[w*h];
	}
	
	public int add(String filepath) {
		GLTexture texture = new GLTexture(filepath);
		System.arraycopy(texture.getRaw(), 0, raw, counter*width*height, width*height);
		raw = Arrays.copyOf(raw, raw.length + width*height);
		
		return counter++;
	}
	
	public void load() {		
		glId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_ARRAY, glId);
		
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);//MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTextureStorage3D(glId, 1, GL_RGBA8, width, height, counter);
		glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0, width, height, counter, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, raw);
		// glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
	}
	
	public void use(int binding, GLProgram program, String handle) {
		glActiveTexture(GL_TEXTURE0 + binding);
		glBindTexture(GL_TEXTURE_2D_ARRAY, glId);
		
		program.setUniform(handle, binding);
	}
}
