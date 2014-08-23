package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class Solid extends GameObject {

	public Solid() {
		super();
		stretched = true;
		setSprite(Sprites.plain);
	}

	public void reset() {}

}
