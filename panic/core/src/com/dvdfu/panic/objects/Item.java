package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.visuals.Sprites;

public class Item extends GameObject {
	private ItemType type;
	private boolean grounded;

	public Item() {
		reset();
		setSize(12, 12);
		stretched = true;
		setSprite(Sprites.plain);
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public ItemType getType() {
		return type;
	}

	public void update() {
		if (!grounded) {
			ySpeed -= 0.2f;
		}
		contain();
		grounded = false;
	}

	private void contain() {
		if (getRight() > Consts.BoundsR) {
			setX(Consts.BoundsR - getWidth());
			xSpeed = -xSpeed;
		}
		if (getX() < Consts.BoundsL) {
			setX(Consts.BoundsL);
			xSpeed = -xSpeed;
		}
	}

	public void act(float delta) {
		update();
		super.act(delta);
	}

	public void draw(Batch batch, float alpha) {
		batch.setColor(new Color(0, 1, 1, 1));
		super.draw(batch, alpha);
		batch.setColor(1, 1, 1, 1);
	}

	public void collideSolid(Floor block) {
		Rectangle myRect = bounds.setPosition(getX(), getY() + ySpeed);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + ySpeed > block.getY() && myRect.y < block.getY()) {
				setY(block.getY() - getHeight());
				ySpeed = 0;
			}
			if (getTop() + ySpeed > block.getTop() && myRect.y < block.getTop()) {
				setY(block.getTop());
				if (ySpeed < -0.1f) {
					ySpeed *= -0.6f;
				} else {
					ySpeed = 0;
				}
				grounded = true;
				if (xSpeed > 0.1f) {
					xSpeed *= 0.6f;
				} else {
					xSpeed = 0;
				}
			}
		}
		myRect.setPosition(getX() + xSpeed, getY());
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + xSpeed > block.getX() && myRect.x < block.getX()) {
				setX(block.getX() - getWidth());
			}
			if (getRight() + xSpeed > block.getRight() && myRect.x < block.getRight()) {
				setX(block.getRight());
			}
		}
	}

	public void reset() {
		type = ItemType.values()[MathUtils.random(ItemType.values().length - 1)];
	}
}
