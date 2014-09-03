package com.dvdfu.panic.visuals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.dvdfu.panic.handlers.Consts;

public class Label {
	private String text;
	private BitmapFont font;

	public Label() {
		this("");
	}

	public Label(String text) {
		this.text = text;
		font = Consts.Test;
		font.setColor(1, 1, 1, 1);
	}

	public int getWidth() {
		return (int) font.getBounds(text).width;
	}

	public int getHeight() {
		return (int) font.getBounds(text).height;
	}

	public void draw(Batch batch, float x, float y) {
		font.setColor(0, 0, 0, 1);
		font.draw(batch, text, x + 1, y - 1);
		font.setColor(1, 1, 1, 1);
		font.draw(batch, text, x, y);
	}

	public void drawC(Batch batch, float x, float y) {
		draw(batch, x - getWidth() / 2, y - getHeight() / 2);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}