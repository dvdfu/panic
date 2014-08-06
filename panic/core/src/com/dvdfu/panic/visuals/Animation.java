package com.dvdfu.panic.visuals;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Animation {
	private Sprite[] frames;
	private float timer;

	public Animation(Sprite reg, int width, int height) {
		frames = new Sprite[(int) (reg.getWidth()) / width];
		timer = 0;
		for (int i = 0; i < frames.length; i++) {
			frames[i] = new Sprite(reg, i * width, 0, width, height);
		}
	}
	
	public void update(float delta) {
		timer += delta * 12;
		while (timer > frames.length) {
			timer -= frames.length;
		}
	}

	public Sprite getFrame() {
		return (frames.length == 1)? frames[0]: frames[(int) timer];
	}

	public Sprite getFrame(int frame) {
		return frames[frame % frames.length];
	}

	public int getWidth() {
		return frames[0].getRegionWidth();
	}

	public int getHeight() {
		return frames[0].getRegionHeight();
	}
}