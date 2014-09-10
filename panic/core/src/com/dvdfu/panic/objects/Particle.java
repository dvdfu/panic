package com.dvdfu.panic.objects;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Enums.ParticleType;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class Particle extends GameObject {
	private ParticleType type;
	private int timer;

	public Particle(GameStage stage) {
		super(stage);
		reset();
		type = ParticleType.TRAIL;
		stretched = true;
	}

	public boolean dead() {
		return timer <= 0;
	}

	public void setType(ParticleType type) {
		this.type = type;
		switch (type) {
		case TRAIL:
			setSprite(Sprites.plain);
			setSize(2, 2);
			timer = 20;
			ySpeed = 0;
			xSpeed = 0;
			break;
		case EXPLOSION:
			setSprite(Sprites.plain);
			setSize(2, 2);
			xSprOffset = 0;
			timer = MathUtils.random(10) + 30;
			ySpeed = MathUtils.random(2f, 5f);
			xSpeed = MathUtils.random(-2f, 2f);
			break;
		case FIRE:
			setSprite(Sprites.fireS);
			setSpriteSpeed(10);
			setSize(4, 4);
			xSprOffset = 0;
			timer = 70 - MathUtils.random(30);
			ySpeed = MathUtils.random(2f, 4f);
			xSpeed = MathUtils.random(-2f, 2f);
			break;
		}
		reset();
	}

	public void act(float delta) {
		switch (type) {
		case TRAIL:
			break;
		case EXPLOSION:
			ySpeed -= 0.3f;
			break;
		case FIRE:
			ySpeed -= 0.1f;
			break;
		}
		timer--;
		super.act(delta);
	}

	public void reset() {
		resetSprite();
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public void update() {
	}

	public void setPosition(float x, float y) {
		setX(x - getWidth() / 2);
		setY(y - getHeight() / 2);
	}

}
