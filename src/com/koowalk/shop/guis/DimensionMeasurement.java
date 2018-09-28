package com.koowalk.shop.guis;

import com.koowalk.shop.util.Container;

public class DimensionMeasurement extends Dimension {
	// Defines the dimension mode
	public enum Mode {
		ABSOLUTE, // Defined by absolute pixel values
		RELATIVE, // Defined by relative percentages
		AUTO      // Defined by its contents
	}
	
	private Container<Dimension> parent;
	private Mode mode;
	private int absolute;
	private double relative;
	
	private boolean updated;
	
	public DimensionMeasurement(Mode m) {
		mode = m;
		absolute = 0;
		relative = 0;
		this.parent = new Container<Dimension>();
	}
	
	public DimensionMeasurement() {
		this(Mode.AUTO);
	}
	
	public void setAbsolute(int absolute) {
		this.updated = this.absolute != absolute;
		mode = Mode.ABSOLUTE;
		this.absolute = absolute;
	}
	
	public void setRelative(double relative) {
		mode = Mode.RELATIVE;
		this.relative = relative;
	}
	
	public void setAuto(int measurement) {
		if (mode == Mode.AUTO) {
			updated = this.absolute != measurement;
			this.absolute = measurement;
		}
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setParent(Container<Dimension> parent) {
		this.parent = parent;
	}
	
	public boolean popUpdate() {
		boolean tmp = updated;
		updated = false;
		return tmp;
	}
	
	public int get() {
		if (mode == Mode.RELATIVE) {
			try {
				int parentValue = (int)(parent.get().get()*relative);
				if (parentValue != absolute) {
					updated = true;
					absolute = parentValue;
				}
			} catch (Exception e) {
				return 0;
			}
		}
		
		return absolute;
	}
}
