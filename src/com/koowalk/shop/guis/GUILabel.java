package com.koowalk.shop.guis;

import com.koowalk.shop.graphics.Font;

public class GUILabel extends GUIComponent {
	private Font font;
	
	public GUILabel(Font font) {
		super(GUITypeIdentifier.TYPE_LABEL);
		this.font = font;
	}
	
	public Font getFont() {
		return font;
	}
}
