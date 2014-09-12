package com.dvdfu.panic.visuals.menus;


public class Scroller extends Button {
	private int range;
	private int index;
	final private String[] values;

	public Scroller(String[] values) {
		super();
		range = values.length;
		this.values = values;
		setText(values[0]);
	}
	
	public void increase() {
		index++;
		if (index >= range) {
			index = 0;
		}
		setText(values[index]);
	}
	
	public void decrease() {
		index--;
		if (index < 0) {
			index = range - 1;
		}
		setText(values[index]);
	}
	
	public void setIndex(int index) {
		this.index = index;
		setText(values[index]);
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getValue() {
		return values[index];
	}
}