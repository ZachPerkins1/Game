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
	public static final int FONT_SHEET_WIDTH = 512;
	
	private ArrayList<Font> fonts;
	private static FontLoader instance = null;
	
	public FontLoader() {
		fonts = new ArrayList<Font>();
	}
	
	public Font load(String fontFile, int size, int min, int max) throws IOException {
		ByteBuffer fontBuffer = readFontFile(fontFile);
		ByteBuffer imgBuffer = MemoryUtil.memAlloc(FONT_SHEET_WIDTH*1024);
		STBTTBakedChar.Buffer charBuffer = STBTTBakedChar.malloc(max - min);
		int maxHeight = stbtt_BakeFontBitmap(fontBuffer, size, imgBuffer, FONT_SHEET_WIDTH, 1024, min, charBuffer);
		MemoryUtil.memFree(fontBuffer);
		GLFontSheet tex = allocateTexture(imgBuffer, FONT_SHEET_WIDTH, maxHeight);
		Font font = new Font(fontFile.substring(0, fontFile.length() - 4), charBuffer, tex, size, min);
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
		ByteBuffer fontBuffer = MemoryUtil.memAlloc((int)fc.size());
		fin.getChannel().read(fontBuffer);
		fin.close();
		fontBuffer.flip();
		return fontBuffer;
	}
	
	private GLFontSheet allocateTexture(ByteBuffer texData, int width, int height) {
		GLFontSheet texture = new GLFontSheet(texData, width, height);
		texture.load();
		return texture;
	}
	
	public static FontLoader getInstance() {
		if (instance == null)
			instance = new FontLoader();
		
		return instance;
	}
}
