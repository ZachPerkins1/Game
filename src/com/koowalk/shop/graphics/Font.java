package com.koowalk.shop.graphics;
import static org.lwjgl.stb.STBTruetype.*;
import org.lwjgl.stb.STBTTAlignedQuad;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

public class Font {
	private static final int LF = 10;
	private static final int CR = 13;
	
	private int size;
	private String name;
	private STBTTBakedChar.Buffer charInfo;
	private STBTTFontinfo fontInfo;
	private GLFontSheet texture;
	private int start;
	
	private int ascent;
	private int descent;
	private int lineGap;
	
	private interface LineStartCalculator {
		int getLineStart(int width, int maxWidth);
	}
	
	public enum Alignment {
		LEFT((width, maxWidth) -> { return 0; }),
		RIGHT((width, maxWidth) -> { return maxWidth - width; }),
		CENTER((width, maxWidth) -> { return (maxWidth - width) / 2; });
		
		private LineStartCalculator calculator;
		
		private Alignment(LineStartCalculator c) {
			calculator = c;
		}
		
		private int getLineStartOffset(int width, int maxWidth) {
			return calculator.getLineStart(width, maxWidth);
		}
	}
	
	// Helper class for fillBuffers
	private class StringLine {
		public String text;
		public int width;

		public StringLine(String text) {
			this.text = text;
			this.width = getWidth(text);
		}
		
		public String toString() {
			return "StringLine text=" + text;
		}
	}
	
	public class FontRenderTarget {
		private StringLine[] lines;
		private int maxWidth;
		private int charCount;
		private int height;
		private int lineGap;
		
		public FontRenderTarget(String text, int wrapWidth, int additionalLineGap) {
			lineGap = additionalLineGap;
			lines = splitLines(text);
			if (wrapWidth >= 0) {
				lines = wrapLines(lines, wrapWidth);
				maxWidth = wrapWidth;
			} else {
				maxWidth = getMaxLineWidth(lines);
			}
			charCount = getCharacterCount(lines);
			height = getHeight(lines.length, lineGap);
		}
		
		public int getBoundingWidth() {
			return maxWidth;
		}
		
		public int getBoundingHeight() {
			return height;
		}
	}
	
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
	
	/**
	 * Given id references to an openGL array buffer pointer and an element array buffer pointer, bind
	 * them and fill them with correct position and texture coordinate for a given FontRenderTarget.
	 * @param arrayBuffer The OpenGL array buffer handle
	 * @param elementArrayBuffer The OpenGL element array buffer handle
	 * @param inX The upper left X position of the text to be rendered
	 * @param inY The upper left Y position of the text to be rendered
	 * @param target The text to render in the form of the font render target see: {@link Font#getRenderTarget(String, int, int)}
	 * @param align The alignment of the text (i.e. left, right, center)
	 */
	public void fillBuffers(int arrayBuffer, int elementArrayBuffer, int inX, int inY, 
			FontRenderTarget target, Alignment align) {
		glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
		
		StringLine[] lines = target.lines;
		int maxWidth = target.maxWidth;
		int count = target.charCount;
		
		float[] data = new float[count * 8 * 2];
		FloatBuffer dataBuffer = FloatBuffer.wrap(data);
		int[] vaos = new int[count * 6];
		IntBuffer attrBuffer = IntBuffer.wrap(vaos);
		
		int indexOffset = 0;
		int yOffset = inY;
		for (int i = 0; i < lines.length; i++) {
			StringLine line = lines[i];
			float[] x = new float[] {inX + align.getLineStartOffset(line.width, maxWidth)};
			float[] y = new float[] {yOffset + ascent + descent};
			
			for (int j = 0; j < line.text.length(); j++) {
				int c = line.text.charAt(j);
				STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
				stbtt_GetBakedQuad(charInfo, texture.getWidth(), texture.getHeight(), c - start, x, y, quad, true);
				dataBuffer.put(quad.x0()).put(quad.y0()).put(quad.s0()).put(quad.t0())
						  .put(quad.x0()).put(quad.y1()).put(quad.s0()).put(quad.t1())
						  .put(quad.x1()).put(quad.y1()).put(quad.s1()).put(quad.t1())
						  .put(quad.x1()).put(quad.y0()).put(quad.s1()).put(quad.t0());
				int index = indexOffset + (4 * j);
				attrBuffer.put(index + 3).put(index + 1).put(index).put(index + 3).put(index + 2).put(index + 1);
				quad.free();
			}
			
			yOffset += ascent + descent + lineGap + target.lineGap;
			indexOffset += line.text.length() * 4;
		}
				
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, vaos, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES*4, 0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES*4, Float.BYTES*2);
		
	}
	
	/**
	 * Generate a new FontRenderTarget for use with fillBuffers. This allows us to front load metric
	 * calculations and only do the bare minimum when we call fillBuffers. It also allows us to have
	 * a back reference to text statistics since font is, on it's own, stateless.
	 * @param string The string to render in the font
	 * @param wrapWidth The width that we want to wrap at. -1 is no wrapping.
	 * @param additionalLineGap The additional gap between each line, by default there is only a minimal
	 * amount of space
	 * @return The new FontRenderTarget 
	 * @see Font#fillBuffers(int, int, int, int, FontRenderTarget, Alignment)
	 */
	public FontRenderTarget getRenderTarget(String string, int wrapWidth, int additionalLineGap) {
		return new FontRenderTarget(string, wrapWidth, additionalLineGap);
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
	
	private int getMaxLineWidth(StringLine[] lines) {
		int maxWidth = 0;
		
		for (int i = 0; i < lines.length; i++) {
			int width = lines[i].width;
			if (width > maxWidth) 
				maxWidth = width;
		}
		
		return maxWidth;
	}
	
	private int getCharacterCount(StringLine[] lines) {
		int total = 0;
		for (StringLine line : lines) 
			total += line.text.length();
		
		return total;
	}
	
	private StringLine[] splitLines(String s) {
		ArrayList<StringLine> builder = new ArrayList<StringLine>(5); // Guess 5
		int lastEnd = 0;
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			if (ch == LF) {
				builder.add(new StringLine(s.substring(lastEnd, i)));
				lastEnd = i + 1;
			} else if (ch == CR) {
				builder.add(new StringLine(s.substring(lastEnd, i)));
				i++;
				lastEnd = i + 1;
			} else if (i == s.length() - 1) {
				builder.add(new StringLine(s.substring(lastEnd, s.length())));
			}
		}
		
		return builder.toArray(new StringLine[builder.size()]);
	}
	
	private StringLine[] wrapLines(StringLine[] lines, int wrapLen) {
		ArrayList<StringLine> builder = new ArrayList<StringLine>(lines.length);
		for (int i = 0; i < lines.length; i++) {
			StringLine line = lines[i];
			int width = lines[i].width;
			if (width <= wrapLen)
				builder.add(lines[i]);
			else {
				wrapLine(line, wrapLen, builder);
			}
		}
		
		return builder.toArray(new StringLine[builder.size()]);
	}
	
	private void wrapLine(StringLine line, int wrapLen, ArrayList<StringLine> builder) {
		int guess = (wrapLen * line.text.length()) / line.width;
		int end = guess;
		int start = 0;
		
		while (start != end) {
			while (end < line.text.length() && getWidth(line.text.substring(start, end)) < wrapLen) {
				end++;
			}
			
			while (end > 0 && getWidth(line.text.substring(start, end)) > wrapLen) {
				end--;
			}
			
			boolean found = false;
			if (end != line.text.length()) {
				for (int i = end; i > start; i--) {
					if (line.text.charAt(i) == ' ') {
						end = i + 1;
						found = true;
						break;
					}
				}
			}
			
			builder.add(new StringLine(line.text.substring(start, found? end - 1: end)));
			
			start = end;
			end = Math.min(line.text.length(), end + guess);
		}
	}
	
	private int getHeight (int lineCount, int additionalSpacing) {
		int gap = additionalSpacing + getLineGap();
		return ((gap + getHeight()) * lineCount) - gap;
	}
}
