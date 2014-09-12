package com.dvdfu.panic.visuals.menus;

import com.dvdfu.panic.visuals.Label;

public class Button extends Label {
	private boolean selected;
	public Button bUp;
	public Button bDown;
	public Button bLeft;
	public Button bRight;

	public Button() {
		super();
	}

	public Button(String text) {
		super(text);
	}

	public void setSelect(boolean selected) {
		this.selected = selected;
		if (this.selected) {
			setColor(1, 0.2f, 0.2f, 1);
		} else {
			setColor(1, 1, 1, 1);
		}
	}
}
