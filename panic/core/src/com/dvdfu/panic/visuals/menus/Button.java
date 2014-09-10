package com.dvdfu.panic.visuals.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.dvdfu.panic.visuals.Label;

public class Button extends Label {
	private boolean selected;
	private float x;
	private float y;
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
			setColor(1, 0.5f, 0.5f, 1);
		} else {
			setColor(1, 1, 1, 1);
		}
	}

	public void draw(Batch batch) {
		super.draw(batch, x, y);
	}

	public void drawC(Batch batch) {
		super.drawC(batch, x, y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
