package com.dvdfu.panic.visuals;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Label {
	private String text;
	private BitmapFont font;
	
	public Label(String text) {
		this.text = text;
		font = new BitmapFont();
	}
	
	public int width() {
		return (int) font.getBounds(text).width;
	}
	
	public int height() {
		return (int) font.getBounds(text).height;
	}
	
	public void draw(Batch batch, float x, float y) {
		font.draw(batch, text, x, y);
	}
	
	public void drawC(Batch batch, float x, float y) {
		font.draw(batch, text, x - width() / 2, y + height() / 2);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}