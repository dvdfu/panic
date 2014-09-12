package com.dvdfu.panic.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dvdfu.panic.handlers.Consts;

public class Label extends Actor {
	private String text;
	private BitmapFont font;
	private Color color;
	private boolean centered;

	public Label() {
		this("");
	}

	public Label(String text) {
		this.text = text;
		font = Consts.Test;
		color = new Color(1, 1, 1, 1);
		font.setColor(color);
	}

	public float getWidth() {
		return font.getBounds(text).width;
	}

	public float getHeight() {
		return font.getBounds(text).height;
	}

	public void draw(Batch batch, float parentAlpha) {
		float x = getX();
		float y = getY();
		if (centered) {
			x -= getWidth() / 2;
			y += getHeight() / 2;
		}
		draw(batch, x, y);
	}

	public void draw(Batch batch, float x, float y) {
		font.setColor(0, 0, 0, 0.75f);
		font.draw(batch, text, x + 1, y - 1);
		font.setColor(color);
		font.draw(batch, text, x, y);
	}
	
	public void setDrawCentered(boolean centered) {
		this.centered = centered;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}
}