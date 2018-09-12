package com.koowalk.shop.guis;

public class DimensionMeasurement implements Dimension {
	// Defines the dimension mode
	public enum Mode {
		ABSOLUTE, // Defined by absolute pixel values
		RELATIVE, // Defined by relative percentages
		AUTO      // Defined by its contents
	}
	
	private DimensionMeasurement parent;
	private Mode mode;
	private int absolute;
	private double relative;
	
	public DimensionMeasurement(Mode m) {
		mode = m;
		absolute = 0;
		relative = 0;
		this.parent = null;
	}
	
	public DimensionMeasurement() {
		this(Mode.AUTO);
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
	
	public void setParent(DimensionMeasurement parent) {
		this.parent = parent;
	}
	
	public int get() {
		if (mode == Mode.ABSOLUTE) 
			return absolute;
		else if (mode == Mode.RELATIVE)
			return (int)(parent.get()*relative);
		
		return 0;
	}
}
