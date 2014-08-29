package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.panic.visuals.Animation;

public abstract class GameObject extends Actor implements Poolable {
	protected Animation sprite;
	private int spriteLength;
	private float spriteTimer;
	protected Rectangle bounds;
	protected boolean stretched;
	protected int xSprOffset;
	protected int ySprOffset;
	protected int sprScale;
	protected float xSpeed;
	protected float ySpeed;

	public GameObject() {
		super();
		bounds = new Rectangle();
		sprScale = 1;
	}

	public void act(float delta) {
		spriteTimer += delta * 6;
		while (spriteTimer > spriteLength) {
			spriteTimer -= spriteLength;
		}
		super.setPosition(getX() + xSpeed, getY() + ySpeed);
		super.act(delta);
		updateBounds();
	}

	public void draw(Batch batch, float parentAlpha) {
		if (sprite != null) {
			int xSpr = (int) (getX() + 0.5f);
			int ySpr = (int) (getY() + 0.5f);
			if (stretched) {
				batch.draw(sprite.getFrame((int) spriteTimer), xSpr, ySpr, getWidth(), getHeight());
			} else if (sprScale > 1) {
				batch.draw(sprite.getFrame((int) spriteTimer), xSpr + xSprOffset * sprScale, ySpr + ySprOffset * sprScale, sprite.getWidth() * sprScale, sprite.getHeight() * sprScale);
			} else {
				batch.draw(sprite.getFrame((int) spriteTimer), xSpr + xSprOffset, ySpr + ySprOffset);
			}
		}
	}

	public void setSprite(Sprite sprite, int width, int height) {
		setSprite(new Animation(sprite, width, height));
	}

	public void setSprite(Animation sprite) {
		if (this.sprite == sprite) {
			return;
		}
		this.sprite = sprite;
		spriteLength = sprite.getLength();
		spriteTimer = 0;
	}

	public void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());
	}

	public Rectangle getBounds() {
		return bounds;
	}
}
