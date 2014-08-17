package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class Solid extends GameObject {

	public Solid(int x, int y) {
		super();
		setPosition(x, y);
		stretched = true;
		setSprite(Sprites.plain);
	}

	public void act(float delta) {
		super.act(delta);
	}

	public void move() {}

	public void reset() {}

}
