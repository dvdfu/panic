package com.dvdfu.panic.objects;

import com.dvdfu.panic.visuals.Sprites;

public class Particle extends GameObject {
	private int timer;
	public Particle() {
		reset();
		stretched = true;
		setSprite(Sprites.plain);
	}
	
	public boolean dead() {
		return timer <= 0;
	}

	public void move() {
		
	}
	
	public void act(float delta) {
		timer--;
		setSize(getWidth() - 1, getHeight() - 1);
		super.act(delta);
	}

	public void reset() {
		timer = 20;
		setSize(20, 20);
	}

}
