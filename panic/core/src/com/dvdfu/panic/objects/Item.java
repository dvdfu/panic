package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.panic.handlers.Consts;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.handlers.GameStage;
import com.dvdfu.panic.visuals.Sprites;

public class Item extends GameObject {
	private ItemType type;
	private boolean grounded;
	private boolean locked;

	public Item(GameStage stage) {
		super(stage);
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
		if (!grounded && !locked) {
			ySpeed -= Consts.Gravity;
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
		if (locked) { return; }
		bounds.setPosition(getX() + xSpeed, getY() + ySpeed);
		if (bounds.overlaps(block.bounds)) {
			if (getY() >= block.getTop()) {
				setY(block.getTop());
				grounded = true;
				if (ySpeed < -1) {
					ySpeed *= -0.6f;
					xSpeed *= 0.6f;
				} else {
					locked = true;
					ySpeed = 0;
					xSpeed = 0;
				}
			}
			if (block.getSolid()) {
				if (getTop() <= block.getY()) {
					setY(block.getY() - getHeight());
					ySpeed = 0;
				}
				if (getX() >= block.getRight()) {
					setX(block.getRight());
					xSpeed = 0;
				} else if (getRight() <= block.getX()) {
					setX(block.getX() - getWidth());
					xSpeed = 0;
				}
			}
		}
		setBounds();
	}

	public void reset() {
		type = ItemType.values()[MathUtils.random(ItemType.values().length - 1)];
		locked = false;
	}
}
