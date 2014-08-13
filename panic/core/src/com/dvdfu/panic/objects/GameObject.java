package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.panic.visuals.Animation;

public abstract class GameObject extends Actor implements Poolable {
	protected Animation sprite;
	protected Rectangle bounds;
	protected boolean stretched;
	protected int xSprOffset;
	protected int ySprOffset;
	protected float x;
	protected float y;
	protected float dx;
	protected float dy;

	public GameObject() {
		super();
		bounds = new Rectangle();
	}

	public void act(float delta) {
		if (sprite != null) {
			sprite.update(delta);
		}
		x += dx;
		y += dy;
		super.setPosition(x, y);
		bounds.set(getX(), getY(), getWidth(), getHeight());
		super.act(delta);
	}

	public void draw(Batch batch, float parentAlpha) {
		if (sprite != null) {
			int xSpr = (int) (getX() + 0.5f);
			int ySpr = (int) (getY() + 0.5f);
			if (stretched) {
				batch.draw(sprite.getFrame(), xSpr, ySpr, getWidth(), getHeight());
			} else {
				batch.draw(sprite.getFrame(), xSpr + xSprOffset, ySpr + ySprOffset);
			}
		}
	}

	public void drawDebug(ShapeRenderer shapes, float parentAlpha) {
		shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void setSprite(Sprite sprite, int width, int height) {
		this.sprite = new Animation(sprite, width, height);
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		super.setPosition(x, y);
	}
}
