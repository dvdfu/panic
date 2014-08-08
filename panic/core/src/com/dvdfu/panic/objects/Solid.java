package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class Solid extends GameObject {

	public Solid(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		stretched = true;
		setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}

	public void act(float delta) {
		super.act(delta);
	}

	public void reset() {}
}
