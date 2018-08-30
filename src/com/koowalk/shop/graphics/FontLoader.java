package com.koowalk.shop.graphics;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import org.lwjgl.system.MemoryUtil;

import org.lwjgl.stb.*;
import static org.lwjgl.stb.STBTruetype.*;

public class FontLoader {
	ArrayList<Font> fonts;
	private static FontLoader instance = null;
	
	public FontLoader() {
		fonts = new ArrayList<Font>();
	}
	
	public Font load(String fontFile, int size, int min, int max) throws IOException {
		ByteBuffer fontBuffer = readFontFile(fontFile);
		fontBuffer.flip();
		ByteBuffer imgBuffer = MemoryUtil.memAlloc(512*512);
		STBTTBakedChar.Buffer charBuffer = STBTTBakedChar.malloc(max - min);
		System.out.println(stbtt_BakeFontBitmap(fontBuffer, size, imgBuffer, 512, 512, min, charBuffer));
		// GLTexture tex = allocateTexture(imgBuffer, 512, 512);
		System.out.println(fontFile.split(".").length);
		Font font = new Font(charBuffer, 0, size, min, fontFile.substring(0, fontFile.length() - 4));
		fonts.add(font);
		return font;
	}
	
	public Font load(String fontFile, int size) throws IOException {
		return load(fontFile, size, 34, 128);
	}
	
	public Font getPreloaded(String fontName, int size) {
		for (Font font : fonts) {
			if (font.getName().equalsIgnoreCase(fontName) && font.getSize() == size) {
				return font;
			}
		}
		
		throw new IllegalArgumentException("Font not found");
	}
	
	public Font get(String fontName, int size) throws IOException {
		try {
			return getPreloaded(fontName, size);
		} catch (IllegalArgumentException e) {
			return load(fontName + ".ttf", size);
		}
	}
	
	private ByteBuffer readFontFile(String fontFile) throws IOException {
		RandomAccessFile fin = new RandomAccessFile(fontFile, "r");
		FileChannel fc = fin.getChannel();
		ByteBuffer fontBuffer = ByteBuffer.allocateDirect((int)fc.size());
		fin.getChannel().read(fontBuffer);
		fin.close();
		return fontBuffer;
	}
	
	private GLTexture allocateTexture(ByteBuffer texData, int width, int height) {
		//GLTexture texture = new GLTexture(texData, width, height);
		// texture.load();
		// return texture;
		return null;
	}
	
	public static FontLoader getInstance() {
		if (instance == null)
			instance = new FontLoader();
		
		return instance;
	}
}
