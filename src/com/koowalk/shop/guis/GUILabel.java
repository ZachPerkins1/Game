package com.koowalk.shop.guis;

import java.awt.Color;
import java.io.IOException;

import com.koowalk.shop.graphics.Font;
import com.koowalk.shop.graphics.FontLoader;
import com.koowalk.shop.guis.DimensionMeasurement.Mode;

public class GUILabel extends GUIComponent {
	private Font font;
	private String text;
	private Color color;
	private	int lineSpacing;
	private Font.FontRenderTarget renderTarget;
	
	private int width;
	private int height;
	
	public GUILabel(Font font, String text, Color color) {
		super(GUITypeIdentifier.TYPE_LABEL);
		this.font = font;
		this.color = color;
		setText(text);
	}
	
	public GUILabel(String font, int size, String text, Color color) throws IOException {
		this(FontLoader.getInstance().get(font, size), text, color);
	}
	
	public GUILabel(String font, int size, Color color) throws IOException {
		this(font, size, "", color);
	}
	
	public GUILabel(String font, int size, String text) throws IOException {
		this(font, size, text, Color.WHITE);
	}
	
	public GUILabel(String font, int size) throws IOException {
		this(font, size, Color.WHITE);
	}
	
	public Font getFont() {
		return font;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return getText();
	}
	
	public Font.FontRenderTarget getRenderTarget() {
		return renderTarget;
	}
	
	public void setText(String text) {
		this.text = text;
		recalculateRenderTarget();
	}
	
	public int getVertexCount() {
		return text.length() * 6;
	}
	
	@Override
	public void update() {
		this.getWidthMeasurement().setAuto(width);
		this.getHeightMeasurement().setAuto(height);
	}
	
	@Override
	public void place() {
		if (getWidthMeasurement().get() != width) {
			recalculateRenderTarget();
		}
		
		this.getHeightMeasurement().setAuto(height);
	}
	
	private void recalculateRenderTarget() {
		updated	= true;
		int wrapWidth = -1;
		if (getWidthMeasurement().getMode() != Mode.AUTO) {
			wrapWidth = getWidthMeasurement().get();
		}
		
		renderTarget = font.getRenderTarget(text, wrapWidth, lineSpacing);
		width = renderTarget.getBoundingWidth();
		height = renderTarget.getBoundingHeight();
	}
}
