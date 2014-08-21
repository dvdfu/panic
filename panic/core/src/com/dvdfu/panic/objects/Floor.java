package com.dvdfu.panic.objects;

public class Floor extends Solid {

	public Floor() {
	}
	
	public void act(float delta) {
		setHeight(getHeight() + 0.2f);
		super.act(delta);
	}
}
