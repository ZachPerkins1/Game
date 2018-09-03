package com.koowalk.shop.graphics;
import static org.lwjgl.stb.STBTruetype.*;
import org.lwjgl.stb.STBTTAlignedQuad;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

public class Font {
	private int size;
	private String name;
	private STBTTBakedChar.Buffer charInfo;
	private STBTTFontinfo fontInfo;
	private GLFontSheet texture;
	private int start;
	
	private int ascent;
	private int descent;
	private int lineGap;
	
	public Font(String name, STBTTBakedChar.Buffer charInfo, STBTTFontinfo fontInfo,
			GLFontSheet texture, int size, int start) {
		this.name = name;
		this.size = size;
		this.charInfo = charInfo;
		this.fontInfo = fontInfo;
		this.texture = texture;
		this.start = start;
		
		getMetrics();
	}
	
	public void fillBuffers(int arrayBuffer, int elementArrayBuffer, int inX, int inY, String text) {
		glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
		
		float[] data = new float[text.length() * 8 * 2];
		FloatBuffer dataBuffer = FloatBuffer.wrap(data);
		int[] vaos = new int[text.length() * 6];
		IntBuffer attrBuffer = IntBuffer.wrap(vaos);
		
		float[] x = new float[] {inX};
		float[] y = new float[] {inY + ascent + descent};
						
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
			stbtt_GetBakedQuad(charInfo, texture.getWidth(), texture.getHeight(), c - start, x, y, quad, true);
			dataBuffer.put(quad.x0()).put(quad.y0()).put(quad.s0()).put(quad.t0())
					  .put(quad.x0()).put(quad.y1()).put(quad.s0()).put(quad.t1())
					  .put(quad.x1()).put(quad.y1()).put(quad.s1()).put(quad.t1())
					  .put(quad.x1()).put(quad.y0()).put(quad.s1()).put(quad.t0());
			int index = 4 * i;
			attrBuffer.put(index + 3).put(index + 1).put(index).put(index + 3).put(index + 2).put(index + 1);
			quad.free();
		}
				
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, vaos, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*4, 0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*4, Float.BYTES*2);
		
	}
	
	public int getHeight() {
		return ascent + descent;
	}
	
	public int getWidth(String s) {
		float[] x = new float[] {0};
		float[] y = new float[] {0};
		
		for (int i = 0; i < s.length(); i++) {
			int c = s.charAt(i);
			STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
			stbtt_GetBakedQuad(charInfo, texture.getWidth(), texture.getHeight(), c - start, x, y, quad, true);
		}
		
		return (int) x[0];
	}
	
	public int getAscent() {
		return ascent;
	}
	
	public int getDescent() {
		return descent;
	}
	
	public int getLineGap() {
		return lineGap;
	}
	
	public GLFontSheet getTexture() {
		return texture;
	}
	
	public void useTexture(GLProgram program, String handle) {
		getTexture().use(program, handle);
	}
	
	public int getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}
	
	private void getMetrics() {
		// Dealing with Java's unfortunate lack of pointers
		int[] a = new int[1];
		int[] d = new int[1];
		int[] l = new int[1];
		
		stbtt_GetFontVMetrics(fontInfo, a, d, l);
		float scaleFactor = (((float)size)/(a[0]-d[0]));
		
		ascent = (int)(a[0] * scaleFactor);
		descent = (int) (d[0] * scaleFactor);
		lineGap = (int) (l[0] * scaleFactor);
	}
}
