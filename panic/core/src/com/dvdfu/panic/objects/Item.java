package com.dvdfu.panic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.panic.handlers.Enums.ItemType;
import com.dvdfu.panic.visuals.Sprites;

public class Item extends GameObject {
	private ItemType type;
	private boolean grounded;

	public Item(float x, float y) {
		reset();
		this.x = x;
		this.y = y;
		setSize(16, 16);
		setSprite(Sprites.atlas.createSprite("plain"), 32, 32);
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public ItemType getType() {
		return type;
	}
	
	public void move() {
		dy -= 0.2f;
		grounded = false;
	}

	public void act(float delta) {
		super.act(delta);
	}

	public void draw(Batch batch, float alpha) {
		batch.setColor(new Color(0, 1, 1, 1));
		super.draw(batch, alpha);
		batch.setColor(1, 1, 1, 1);
	}

	public void collideSolid(Solid block) {
		if (grounded) {
			return;
		}
		Rectangle myRect = bounds.setPosition(x, y + dy);
		if (myRect.overlaps(block.bounds)) {
			if (getTop() + dy > block.getY() && myRect.y < block.getY()) {
				y = block.getY() - getHeight();
				dy = 0;
			}
			if (getTop() + dy > block.getTop() && myRect.y < block.getTop()) {
				y = block.getTop();
				dy = 0;
				grounded = true;
			}
		}
		myRect.setPosition(x + dx, y);
		if (myRect.overlaps(block.bounds)) {
			if (getRight() + dx > block.getX() && myRect.x < block.getX()) {
				x = block.getX() - getWidth();
			}
			if (getRight() + dx > block.getRight() && myRect.x < block.getRight()) {
				x = block.getRight();
			}
		}
	}

	public void reset() {}
}
