package com.koowalk.shop.guis;

import java.io.IOException;

import com.koowalk.shop.graphics.Font;
import com.koowalk.shop.graphics.FontLoader;

public class GUILabel extends GUIComponent {
	private Font font;
	private String text;
	
	public GUILabel(Font font) {
		super(GUITypeIdentifier.TYPE_LABEL);
		this.font = font;
	}
	
	public GUILabel(String font, int size) throws IOException {
		this(FontLoader.getInstance().get(font, size));
	}
	
	public Font getFont() {
		return font;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
