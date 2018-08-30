package com.koowalk.shop.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class GLFontSheet extends GLImage {
	ByteBuffer imageData; // Gets released after loading
	
	public GLFontSheet(ByteBuffer imageData, int width, int height) {
		super(width, height);
		this.imageData = imageData;
		
	}
	
	@Override
	public void load() {
		glId = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, imageData);
		MemoryUtil.memFree(imageData);
	}
}
