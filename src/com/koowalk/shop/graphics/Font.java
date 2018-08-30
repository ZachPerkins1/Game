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

import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Font {
	private int size;
	private String name;
	private STBTTBakedChar.Buffer charInfo;
	private int texture;
	private int start;
	
	public Font(STBTTBakedChar.Buffer charInfo, int tex, int size, int start, String name) {
		this.name = name;
		this.size = size;
		this.charInfo = charInfo;
		this.texture = tex;
		this.start = start;
	}
	
	public void fillBuffers(int arrayBuffer, int elementArrayBuffer, int inX, int inY, String text) {
		// glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
		// glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
		
		float[] data = new float[text.length() * 8 * 2];
		FloatBuffer dataBuffer = FloatBuffer.wrap(data);
		int[] vaos = new int[text.length() * 6];
		IntBuffer attrBuffer = IntBuffer.wrap(vaos);
		
		float[] x = new float[] {inX};
		float[] y = new float[] {inY};
				
		text.chars().forEach((int i) -> {
			STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
			stbtt_GetBakedQuad(charInfo, 512, 512, i - start, x, y, quad, true);
			dataBuffer.put(quad.x0()).put(quad.y0()).put(quad.t0()).put(quad.s0())
					  .put(quad.x0()).put(quad.y1()).put(quad.t0()).put(quad.s1())
					  .put(quad.x1()).put(quad.y1()).put(quad.t1()).put(quad.s1())
					  .put(quad.x1()).put(quad.y0()).put(quad.t1()).put(quad.s0());
			attrBuffer.put(3).put(1).put(0).put(3).put(2).put(1);
			quad.free();
		});
		
		System.out.println(Arrays.toString(data));
	}
	
	public int getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}
}
