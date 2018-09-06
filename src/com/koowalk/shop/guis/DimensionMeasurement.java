package com.koowalk.shop.guis;

public class DimensionMeasurement {
	// Defines the dimension mode
	public enum Mode {
		ABSOLUTE, // Defined by absolute pixel values
		RELATIVE, // Defined by relative percentages
		AUTO      // Defined by its contents
	}
	
	private Mode mode;
	private int absolute;
	private double relative;
	
	public DimensionMeasurement() {
		mode = Mode.AUTO;
		absolute = 0;
		relative = 0;
	}
	
	public void setAbsolute(int absolute) {
		this.absolute = absolute;
	}
	
	public void setRelative(double relative) {
		this.relative = relative;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public int get(int parent) {
		if (mode == Mode.ABSOLUTE) {
			return absolute;
		} else if (mode == Mode.RELATIVE) {
			return (int)((relative/100)*parent);
		}
		
		return 0;
	}
	
	public int get() {
		return get(0);
	}
}
