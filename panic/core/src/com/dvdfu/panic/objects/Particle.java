package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Enums.ParticleType;
import com.dvdfu.panic.visuals.Sprites;

public class Particle extends GameObject {
	private ParticleType type;
	private int timer;
	public Particle() {
		reset();
		type = ParticleType.TRAIL;
		stretched = true;
		setSprite(Sprites.plain);
	}
	
	public boolean dead() {
		return timer <= 0;
	}
	
	public void setType(ParticleType type) {
		this.type = type;
		switch (type) {
		case TRAIL:
			setSize(12, 12);
			timer = 20;
			dy = 0;
			dx = 0;
			break;
		case EXPLOSION:
			setSize(6, 6);
			timer = 40;
			dy = MathUtils.random(4f, 7f);
			dx = MathUtils.random(-3f, 3f);
			break;
		}
		reset();
	}
	
	public void act(float delta) {
		switch (type) {
		case TRAIL:
			break;
		case EXPLOSION:
			dy -= 0.3f;
			break;
		}
		timer--;
		super.act(delta);
	}

	public void reset() {
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public void move() {}
	
	public void setPosition(float x, float y) {
		this.x = x - getWidth() / 2;
		this.y = y - getHeight() / 2;
		super.setPosition(this.x, this.y);
	}

}
