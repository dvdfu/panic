package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.dvdfu.panic.visuals.Animation;

public abstract class GameObject extends Actor {
	private Animation sprite;
	protected float x;
	protected float y;

	public GameObject() {
		super();
		x = 0;
		y = 0;
	}

	public void act(float delta) {
		if (sprite != null) {
			sprite.update(delta);
		}
		setPosition(x, y);
		super.act(delta);
	}

	public void draw(Batch batch, float parentAlpha) {
		if (sprite != null) {
			int xSpr = (int) (getX() + 0.5f);
			int ySpr = (int) (getY() + 0.5f);
			batch.draw(sprite.getFrame(), xSpr, ySpr, getWidth(), getHeight());
		}
	}

	public void setSprite(Sprite sprite, int width, int height) {
		this.sprite = new Animation(sprite, width, height);
	}
}
